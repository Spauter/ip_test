package com.bloducspauter.intercept.config;

import com.bloducspauter.base.entities.FacilityInformation;
import com.bloducspauter.intercept.service.FacilityInformationService;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 通过拦截器处理{@code HttpServletRequest}里面的请求IP,IP Address,请求的地址进行处理
 * 拦截器的作用可能是限制请求的频率，以防止过多的请求对服务器造成负载过高的情况
 * 拦截器会在请求Controller层前被调用，除了{@code afterCompletion}
 * @author Bloduc Sputer
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RateLimitInterceptor.class);
    // 同一时间段内允许的最大请求数
    private static final int MAX_REQUESTS = 100;
    // 时间段，单位为毫秒 在一分钟内限制ip访问次数为20次
    private static final long TIME_PERIOD = 60 * 1000;

    private final ConcurrentHashMap<Integer, Integer> requestCounts = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Resource
    private FacilityInformationService requestEntityService;

    /**
     * 这个方法将在请求处理之前进行调用。注意：如果该方法的返回值为false ，
     * 将视为当前请求结束，不仅自身的拦截器会失效，还会导致其他的拦截器也不再执行。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        return true;
    }

    /**
     * 会在Controller 中的方法调用之后，DispatcherServlet 返回渲染视图之前被调用。
     * 有意思的是：postHandle() 方法被调用的顺序跟 preHandle() 是相反的，
     * 先声明的拦截器 preHandle() 方法先执行，而postHandle()方法反而会后执行。
     * 需要前者{@link #preHandle(HttpServletRequest, HttpServletResponse, Object)}返回结果为true时才会执行此方法
     * @param request 请求
     * @param response 回复
     * @param handler 请求头
     * @throws Exception 包括{@code IOException}、{@code SQLException}
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        String ipAddress = getIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        UserAgent userAgentObj = UserAgent.parseUserAgentString(userAgent);
        int id = userAgentObj.getId();
        String address = ipAddress.split(",")[0];
        // 更新 IP 地址的请求数
        requestCounts.put(id, requestCounts.getOrDefault(id, 0) + 1);
        //如果当前访问就开始统计
        if (!requestCounts.contains(id)) {
            countAfterIntercept(id, ipAddress, address, userAgentObj);
        }
        // 检查 IP 地址是否已经达到最大请求数
        if (requestCounts.containsKey(id) && requestCounts.get(id) >= MAX_REQUESTS) {
            //打印信息
            log.info("Id:{}, Ip Address:{}, Browser:{} ,Operating_system:{} has many requests",
                    id, ipAddress, userAgentObj.getBrowser().getName(), userAgentObj.getOperatingSystem().getName());
            //设置响应状态码
            response.setStatus(429);
            response.getWriter().write("Too many requests from this IP address");
        }
    }

    /**
     * 在请求中分割获取到ip地址
     * @param request {@code HttpServletRequest}
     * @return ip地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        log.debug("IP Address:{}",ipAddress);
        return ipAddress;
    }

    /**
     * 开始统计设备id访问次数，并存入数据库
     * @param id 设备ID
     * @param ipAddress IP地址
     * @param address 请求的地址
     * @param userAgentObj 请求的{@code UserAgent}
     */
    private void countAfterIntercept(Integer id, String ipAddress, String address, UserAgent userAgentObj) {
        // 在指定时间后清除 IP 地址的请求数
        scheduler.schedule(() -> {
            int totalRequests = requestCounts.get(id);
            try {
                int rejectedRequest = Math.max(totalRequests - 20, 0);
                String browser = userAgentObj.getBrowser().getName();
                String operatingSystem = userAgentObj.getOperatingSystem().getName();
                FacilityInformation requestFacilityInformation = new FacilityInformation(id, operatingSystem, browser, totalRequests, rejectedRequest, address,0);
                requestEntityService.insert(requestFacilityInformation);
            } catch (Exception e) {
               log.error(e.getLocalizedMessage());
            }
            requestCounts.remove(id);
            log.info("Id {} can request again", id);
        }, TIME_PERIOD, TimeUnit.MILLISECONDS);
    }
}
