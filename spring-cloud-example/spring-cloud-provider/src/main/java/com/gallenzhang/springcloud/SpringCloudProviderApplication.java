package com.gallenzhang.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * <p>
 * 提供者启动类
 * </p>
 *
 * @author gallenzhang
 * @since 2020/6/20
 **/
@SpringBootApplication
@EnableEurekaClient
public class SpringCloudProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudProviderApplication.class, args);
    }
}
