package com.gallenzhang.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Hello Controller
 * </p>
 *
 * @author gallenzhang
 * @since 2020/6/20
 **/
@RestController
public class HelloController {

    @Value("${server.port}")
    private String port;

    @RequestMapping("/hello")
    public String sayHello(@RequestParam(value = "name", defaultValue = "gallenzhang") String name){
        return "hello " + name + " , I am from port:" + port;
    }
}
