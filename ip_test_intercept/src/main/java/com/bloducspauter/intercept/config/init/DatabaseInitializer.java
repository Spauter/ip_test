package com.bloducspauter.intercept.config.init;

import com.bloducspauter.intercept.mapper.FacilityInformationCurrentRequestMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 初始化数据库
 * @author Bloduc Spauter
 *
 */
@Component
@Slf4j
public class DatabaseInitializer {
    @Resource
    FacilityInformationCurrentRequestMapper mapper;

    @PostConstruct
    public void init() {
        log.info("Starting truncate the table current_request_entity");
        try {
           mapper.truncate();
        } catch (Exception e) {
            log.warn("Truncate failed because of {}:{}", e.getClass().getSimpleName(), e.getMessage());
        }finally {
            log.info("Finished to truncate the database initializer");
        }
    }
}

