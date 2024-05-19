package com.bloducspauter.intercept.config.intercept;

import com.bloducspauter.base.entities.FacilityInformation;
import com.bloducspauter.base.utils.GetIpUtil;
import com.bloducspauter.intercept.service.FacilityInformationCurrentRequestService;
import com.bloducspauter.intercept.service.FacilityInformationService;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

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
    // 同一时间段内允许的最大请求数
    private static final int MAX_REQUESTS = 120;
    // 时间段，单位为毫秒 在一分钟内限制ip访问次数为20次
    private static final long TIME_PERIOD = 10 * 1000;

    private final Map<String, Integer> requestCounts = new ConcurrentSkipListMap<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Resource
    private FacilityInformationService requestEntityService;

    @Resource
    private FacilityInformationCurrentRequestService facilityInformationService;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 这个方法将在请求处理之前进行调用。注意：如果该方法的返回值为false ，
     * 将视为当前请求结束，不仅自身的拦截器会失效，还会导致其他的拦截器也不再执行。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
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
        } else if (requestCounts.get(ipAddress) == MAX_REQUESTS) {
            //打印信息
            log.info("Id:{}, Ip Address:{}, Browser:{} ,Operating_system:{} has many requests",
                    id, ipAddress, userAgentObj.getBrowser().getName(), userAgentObj.getOperatingSystem().getName());
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

    /**
     * 会在Controller 中的方法调用之后，DispatcherServlet 返回渲染视图之前被调用。
     * 有意思的是：postHandle() 方法被调用的顺序跟 preHandle() 是相反的，
     * 先声明的拦截器 preHandle() 方法先执行，而postHandle()方法反而会后执行。
     * 需要前者{@link #preHandle(HttpServletRequest, HttpServletResponse, Object)}返回结果为true时才会执行此方法
     *
     * @param request  请求
     * @param response 回复
     * @param handler  请求头
     * @throws Exception 包括{@code IOException}、{@code SQLException}
     */
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
//
//    }

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
            int totalRequests = requestCounts.get(address);
            try {
                int rejectedRequest = Math.max(totalRequests - MAX_REQUESTS, 0);
                String browser = userAgentObj.getBrowser().getName();
                String operatingSystem = userAgentObj.getOperatingSystem().getName();
                FacilityInformation requestFacilityInformation = new FacilityInformation(id, operatingSystem, browser, totalRequests, rejectedRequest, address, 0);
                requestEntityService.insert(requestFacilityInformation);
                facilityInformationService.addFacilityInformation(requestFacilityInformation);
            } catch (Exception e) {
                log.error(e.getLocalizedMessage());
            }
            //Map中移除
            requestCounts.remove(address);
            isNew = true;
            log.info("address {} can request again", address);
        }, TIME_PERIOD, TimeUnit.MILLISECONDS);
    }
}
