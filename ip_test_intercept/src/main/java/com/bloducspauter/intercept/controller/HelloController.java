package com.bloducspauter.intercept.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.annotation.AccessType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 测试用
 * @author Bloduc Spauter
 *
 */
@Api("Hello测试控制层")
@RestController
public class HelloController {
    @ApiOperation("Hello!")
    @GetMapping("hello")
    public String hello() {
        return "Hello!This is a simple SpringBoot application";
    }
}
