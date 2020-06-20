package com.gallenzhang.springcloud.controller;

import com.gallenzhang.springcloud.client.HelloClient;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private HelloClient helloClient;

    @RequestMapping(value = "/hello")
    public String sayHello(@RequestParam String name) {
        return helloClient.sayHelloFromClient(name);
    }

}
