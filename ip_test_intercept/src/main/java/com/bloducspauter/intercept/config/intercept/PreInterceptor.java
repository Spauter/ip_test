package com.bloducspauter.intercept.config.intercept;


import com.bloducspauter.base.entities.FacilityInformation;
import com.bloducspauter.base.utils.GetIpUtil;
import com.bloducspauter.intercept.service.FacilityInformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

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

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private boolean isRequested = false;

    private static final long TIME_PERIOD = 60 * 1000;

    private final Map<Integer, Integer> blacklists = new ConcurrentSkipListMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        int id = new GetIpUtil().getId(request);
        Object storedRedisId = redisTemplate.opsForValue().get(id + "");
        if (storedRedisId != null) {
            if (!isRequested) {
                updateBlacklistRequests(id);
                isRequested = true;
            }
            blacklists.put(id, blacklists.getOrDefault(id, 0) + 1);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().println("403 Forbidden");
            return false;
        } else {
            return true;
        }
    }

    private void updateBlacklistRequests(int id) {
        scheduler.schedule(() -> {
            try {
                int totalRequests = blacklists.get(id);
                FacilityInformation facilityInformation = service.findById(id);
                int originTotalRequests = facilityInformation.getTotalRequest();
                int originRejectRequests = facilityInformation.getRejectedRequest();
                facilityInformation.setTotalRequest(totalRequests + originTotalRequests);
                facilityInformation.setRejectedRequest(totalRequests + originRejectRequests);
                service.update(facilityInformation);
            } catch (Exception e) {
                log.error(e.getLocalizedMessage());
            } finally {
                isRequested = false;
            }
            //Map中移除
        }, TIME_PERIOD, TimeUnit.MILLISECONDS);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        String path = request.getServletPath();
        log.info(path);
        if (!path.matches("current_total")) {
            return;
        }
        List<String> newBlackList;
        try {
             newBlackList= (List<String>) redisTemplate.opsForValue().get("newBlackList");
        } catch (Exception e) {
            log.warn("Adding failed because of {}",e.getClass().getSimpleName());
            log.debug(e.getLocalizedMessage());
            return;
        }
        if (newBlackList == null) {
            log.warn("No new blacklist entities to add");
            return;
        }
        newBlackList.forEach(id->{
            int sid= Integer.parseInt(id);
            log.info("adding {} into blacklist",sid);
            blacklists.put(sid,sid);
        });
    }


    /**
     * 提前获取被禁止的对象,并加载到redis缓存里面
     * {@code PostConstruct}在springBoot启动时会执行并且只会执行一次
     */
    @PostConstruct
    public void loadBlacklistEntities() {
        log.info("starting loading all forbidden entities");
        try {
            List<Object> list = service.getForbiddenEntities();
            if (list.isEmpty()) {
                log.warn("Empty forbidden entities");
                return;
            }
            for (Object o : list) {
                log.info("Adding {} into the blacklist", o.toString());
                int id = Integer.parseInt(o.toString());
                blacklists.put(id, 0);
                redisTemplate.opsForValue().set(o.toString(), o);
            }
        } catch (Exception e) {
            log.warn("Exception thread in main:{} {}", e.getClass().getSimpleName(), e.getMessage());
            log.warn("loading  forbidden entities failed");
        }
    }
}