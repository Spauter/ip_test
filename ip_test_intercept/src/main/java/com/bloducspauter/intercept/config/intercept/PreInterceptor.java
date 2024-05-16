package com.bloducspauter.intercept.config.intercept;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bloducspauter.base.entities.FacilityInformation;
import com.bloducspauter.base.page.PageParams;
import com.bloducspauter.intercept.service.FacilityInformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Bloduc Spauter
 */
@Component
@Slf4j
public class PreInterceptor implements HandlerInterceptor {
    @Resource
    FacilityInformationService service;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @PostConstruct
    public void loadForbiddenEntities() {
        log.info("starting loading all forbidden entities");
        try {
            List<FacilityInformation> list = service.getForbiddenEntities();
            if (list.isEmpty()) {
                log.warn("Empty forbidden entities");
            }
            redisTemplate.opsForValue().set("forbidden_entities",list);
        } catch (Exception e) {
            log.debug("reason", e.getCause());
            log.error("loading  forbidden entities failed");
        }
    }
}