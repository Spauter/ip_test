package com.bloducspauter.intercept.config.init;

import com.bloducspauter.intercept.mapper.FacilityInformationCurrentRequestMapper;
import com.bloducspauter.intercept.mapper.FacilityInformationMapper;
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

    @Resource
    FacilityInformationMapper facilityInformationMapper;
    @PostConstruct
    public void init() {
        log.info("Starting truncate the table");
        try {
           mapper.truncate();
           facilityInformationMapper.truncate();
        } catch (Exception e) {
            log.warn("Truncate failed because of {}:{}", e.getClass().getSimpleName(), e.getMessage());
        }finally {
            log.info("Finished to truncate the database initializer");
        }
    }
}

