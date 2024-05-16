package com.bloducspauter.intercept.config;

import com.bloducspauter.intercept.config.intercept.PreInterceptor;
import com.bloducspauter.intercept.config.intercept.RateLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 *
 * @author Bloduc Spauter
 *
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final RateLimitInterceptor rateLimitInterceptor;

    private final PreInterceptor preInterceptor;

    @Autowired
    public WebMvcConfig(RateLimitInterceptor rateLimitInterceptor, PreInterceptor preInterceptor) {
        this.preInterceptor = preInterceptor;
        this.rateLimitInterceptor = rateLimitInterceptor;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //请按照顺序添加拦截器
        registry.addInterceptor(preInterceptor);
        registry.addInterceptor(rateLimitInterceptor);
    }
}
