package com.gallenzhang.springcloud.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * Hello Client
 * </p>
 *
 * @author gallenzhang
 * @since 2020/6/20
 **/
@FeignClient(value = "spring-cloud-provider")
public interface HelloClient {

    /**
     * 客户端调用服务
     * @param name
     * @return
     */
    @RequestMapping(value = "/hello")
    String sayHelloFromClient(@RequestParam(value = "name") String name);
}
