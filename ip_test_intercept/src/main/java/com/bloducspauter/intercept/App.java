package com.bloducspauter.intercept;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
/**
 * 启动类
 * @author Bloduc Spauter
 *
 */
@SpringBootApplication(scanBasePackages={"com.bloducspauter.intercept"})
@MapperScan(value = {"com.bloducspauter.intercept.mapper"})
@EnableRedisHttpSession
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}