package com.bloducspauter.intercept;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
/**
 * 启动类
 * @author Bloduc Spauter
 *
 */
@SpringBootApplication(scanBasePackages={"com.bloducspauter.intercept"})
@MapperScan(value = {"com.bloducspauter.intercept.mapper"})
@EnableRedisHttpSession
@EnableDiscoveryClient
public class InterceptApplication {
    public static void main(String[] args) {
        SpringApplication.run(InterceptApplication.class, args);
    }
}
