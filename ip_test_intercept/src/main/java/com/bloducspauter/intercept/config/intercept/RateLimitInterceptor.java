package com.bloducspauter.intercept.config.intercept;

import com.bloducspauter.base.entities.FacilityInformation;
import com.bloducspauter.base.utils.GetIpUtil;
import com.bloducspauter.intercept.service.FacilityInformationCurrentRequestService;
import com.bloducspauter.intercept.service.FacilityInformationService;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 通过拦截器处理{@code HttpServletRequest}里面的请求IP,IP Address,请求的地址进行处理
 * 拦截器的作用可能是限制请求的频率，以防止过多的请求对服务器造成负载过高的情况
 * 拦截器会在请求Controller层前被调用，除了{@code afterCompletion}
 *
 * @author Bloduc Sputer
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private boolean isNew = true;

    private static final Logger log = LoggerFactory.getLogger(RateLimitInterceptor.class);

    private static int MAX_REQUESTS = 30;

    private static final long TIME_PERIOD = 60 * 1000;

    private final Map<String, Integer> requestCounts = new ConcurrentSkipListMap<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Resource
    private FacilityInformationService requestEntityService;

    @Resource
    private FacilityInformationCurrentRequestService facilityInformationService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 这个方法将在请求处理之前进行调用。注意：如果该方法的返回值为false ，
     * 将视为当前请求结束，不仅自身的拦截器会失效，还会导致其他的拦截器也不再执行。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String origin = request.getParameter("diy_name");
        if ("Bloduc Spauter".equalsIgnoreCase(origin)) {
            return true;
        }
        GetIpUtil getIpUtil = new GetIpUtil();
        String ipAddress = getIpUtil.getIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        UserAgent userAgentObj = UserAgent.parseUserAgentString(userAgent);
        int id = userAgentObj.getId();
        // 更新 IP 地址的请求数
        requestCounts.put(ipAddress, requestCounts.getOrDefault(ipAddress, 0) + 1);
        //如果当前访问就开始统计
        if (isNew) {
            countAfterIntercept(id, ipAddress, userAgentObj);
            isNew = false;
            return true;
        } else if (requestCounts.get(ipAddress) == MAX_REQUESTS ) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/verify.html?diy_name=Bloduc+Spauter");
            //执行转发
            dispatcher.forward(request, response);
//            存入并发哈希表
            remove(id,ipAddress,userAgentObj);
            //打印信息
            log.info("Id:{}, Ip Address:{}, Browser:{} ,Operating_system:{} has many requests",
                    id, ipAddress, userAgentObj.getBrowser().getName(), userAgentObj.getOperatingSystem().getName());
            return false;
        }
        // 检查 IP 地址是否已经达到最大请求数
        else if (requestCounts.get(ipAddress) > MAX_REQUESTS) {
            //设置响应状态码
            response.setStatus(429);
            response.getWriter().write("Too many requests from this IP address");
            return false;
        }
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        String path = request.getServletPath();
        if (path.endsWith("max_allow")) {
            maxAllow();
        } else if (path.endsWith("commit_form")) {
            protect();
        }
    }

    public void maxAllow() {
        Object max = redisTemplate.opsForValue().get("max_allow");
        log.info("Updating MAX_REQUESTS");
        if (max == null) {
            log.warn("No data,using default value {}", 1000);
            return;
        }
        try {
            String maxAllow = max.toString();
            MAX_REQUESTS = Integer.parseInt(maxAllow);
            log.info("updating finished: {}", maxAllow);
        } catch (Exception e) {
            log.error("Updating failed because of {},{}", e.getClass().getSimpleName(), e.getMessage());
            log.warn("No data,using default value {}", 1000);
        }
    }

    public void protect() {
        Object webAddress = redisTemplate.opsForValue().get("webAddress");
        if (webAddress == null) {
            log.warn("No website address");
            return;
        }
        log.info("web address {}", webAddress);
    }

    /**
     * 开始统计设备id访问次数，并存入数据库
     *
     * @param id           设备ID
     * @param address      请求的地址
     * @param userAgentObj 请求的{@code UserAgent}
     */
    private void countAfterIntercept(Integer id, String address, UserAgent userAgentObj) {
        // 在指定时间后清除 IP 地址的请求数
        scheduler.schedule(() -> {
            try {
                count(id, address, userAgentObj);
                isNew=true;
            } catch (Exception e) {
                log.error(e.getLocalizedMessage());
            }
        }, 5*1000, TimeUnit.MILLISECONDS);
    }

    /**
     * 从{@code requestCounts}移除
     */
    private void remove(int id,String ipAddress,UserAgent userAgentObj) {
        scheduler.schedule(() -> {
            //Map中移除
            count(id, ipAddress, userAgentObj);
            requestCounts.remove(ipAddress);
            log.info("address {} can request again", ipAddress);
            isNew=true;
        }, TIME_PERIOD, TimeUnit.MILLISECONDS);
    }

    /**
     * 统计访问次数
     * @param id 设备id
     * @param address IP地址
     * @param userAgentObj {@code UserAgent}
     */
    private void count(Integer id, String address, UserAgent userAgentObj) {
        try {
            if (!requestCounts.containsKey(address)) {
                return;
            }
            int totalRequests = requestCounts.get(address);
            int rejectedRequest = Math.max(totalRequests - MAX_REQUESTS, 0);
            String browser = userAgentObj.getBrowser().getName();
            String operatingSystem = userAgentObj.getOperatingSystem().getName();
            FacilityInformation requestFacilityInformation = new FacilityInformation(id, operatingSystem, browser, totalRequests, rejectedRequest, address, 0);
            requestEntityService.insert(requestFacilityInformation);
            facilityInformationService.addFacilityInformation(requestFacilityInformation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
