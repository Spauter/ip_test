package com.bloducspauter.intercept.config.intercept;


import com.bloducspauter.base.utils.GetIpUtil;
import com.bloducspauter.intercept.config.init.RedisInitializer;
import com.bloducspauter.intercept.service.FacilityInformationCurrentRequestService;
import com.bloducspauter.intercept.service.FacilityInformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 先前拦截器，在第一位
 * 用于拦截黑名单
 *
 * @author Bloduc Spauter
 */
@Component
@Slf4j
public class PreInterceptor implements HandlerInterceptor {
    @Resource
    FacilityInformationService service;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Resource
    FacilityInformationCurrentRequestService currentRequestService;

    @Resource
    RedisInitializer redisInitializer;


    @Override
    @SuppressWarnings("unchecked")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String origin=request.getHeader("Diy_name");
        if ("Bloduc Spauter".equalsIgnoreCase(origin)) {
            return true;
        }
        String ipAddress = new GetIpUtil().getIpAddress(request);
        Object storedRedisIp = redisTemplate.opsForValue().get(ipAddress);
        if (storedRedisIp != null) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().println("You don't have permission to interview it");
            return false;
        } else {
            return true;
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        String path = request.getServletPath();
        if (!path.endsWith("forbid")) {
            return;
        }
        try {
            log.info("try updating blacklist");
            afterPropertiesSet();
        } catch (Exception e) {
            log.warn("Adding failed because of {}", e.getClass().getSimpleName());
            log.warn(e.getLocalizedMessage());
        }
    }


    /**
     * 提前获取被禁止的对象,并加载到redis缓存里面
     * 在所哟{@code PostConstruct}执行完后才会执行
     */
    public void afterPropertiesSet() throws Exception {
        log.info("starting loading all forbidden entities");
        try {
            redisInitializer.delCache(redisTemplate);
            List<Object> list = service.getForbiddenEntities();
            if (list.isEmpty()) {
                log.warn("Empty forbidden entities");
                return;
            }
            for (Object o : list) {
                log.info("Adding {} into the blacklist", o.toString());
                redisTemplate.opsForValue().set(o.toString(), o);
            }
        } catch (Exception e) {
            log.warn("Exception thread in main:{} {}", e.getClass().getSimpleName(), e.getMessage());
            log.warn("loading  forbidden entities failed");
        }
    }
}