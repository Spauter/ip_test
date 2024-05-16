package com.bloducspauter.intercept.config.intercept;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bloducspauter.base.entities.FacilityInformation;
import com.bloducspauter.base.page.PageParams;
import com.bloducspauter.intercept.service.FacilityInformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

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
        } catch (Exception e) {
            log.debug("reason", e.getCause());
            log.error("loading  forbidden entities failed");
        }
    }
}