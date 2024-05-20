package com.bloducspauter.index.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试用
 * @author Bloduc Spauter
 *
 */
@RestController
public class HelloController {
    @GetMapping("hello")
    public String hello() {
        return "Hello! This is a simple SpringBoot application";
    }
}
