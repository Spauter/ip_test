package com.bloducspauter.intercept.config.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.Set;

/**
 * 初始化redis任务
 *
 * @author Bloduc Spauter
 */
@Component
@Slf4j
public class RedisInitializer {
    /**
     * 清空redis里面所有的数据
     * @param redisTemplate
     * @return
     */
    @Bean
    public boolean delCache(RedisTemplate<String,Object> redisTemplate) {
        log.warn("start removing all keys");
        Set<String> keys = redisTemplate.keys("*");
        if (keys == null) {
            log.warn("No redis data to delete");
            return false;
        }
        for (String key : keys) {
            log.debug("removing key:{}",key);
            redisTemplate.delete(key);
        }
        log.info("removed all keys");
        return true;
    }

}
