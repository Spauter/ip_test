package com.bloducspauter.intercept.config.intercept;


import com.bloducspauter.base.entities.FacilityInformation;
import com.bloducspauter.base.utils.GetIpUtil;
import com.bloducspauter.intercept.config.init.RedisInitializer;
import com.bloducspauter.intercept.service.FacilityInformationCurrentRequestService;
import com.bloducspauter.intercept.service.FacilityInformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
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
public class PreInterceptor implements HandlerInterceptor, InitializingBean {
    @Resource
    FacilityInformationService service;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Resource
    FacilityInformationCurrentRequestService currentRequestService;

    @Resource
    RedisInitializer redisInitializer;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private boolean isRequested = false;

    private static final long TIME_PERIOD = 5 * 1000;

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
            response.getWriter().println("You don't have permission to interview ut");
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
                currentRequestService.addFacilityInformation(facilityInformation);
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
        if (!path.endsWith("current_total")) {
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
        blacklists.clear();
        newBlackList.forEach(id->{
            int sid= Integer.parseInt(id);
            log.info("adding {} into blacklist",sid);
            blacklists.put(sid,sid);
        });
    }


    /**
     * 提前获取被禁止的对象,并加载到redis缓存里面
     * 在所哟{@code PostConstruct}执行完后才会执行
     */
    @Override
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