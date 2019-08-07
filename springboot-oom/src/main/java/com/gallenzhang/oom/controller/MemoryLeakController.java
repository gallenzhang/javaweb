package com.gallenzhang.oom.controller;

import com.gallenzhang.oom.constant.ResultConstants;
import com.gallenzhang.oom.po.UserInfoPO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author : zhangxq
 * @date : 2019/08/06
 * @description : 内存泄露示例
 */
@RestController
public class MemoryLeakController {
    /**
     * 计数器
     */
    private static final AtomicLong count = new AtomicLong(0L);

    /**
     * 存放用户信息map
     */
    private static final Map<Long, UserInfoPO> map = new HashMap();


    /**
     *
     * 启动命令:
     * java -Xms100m -Xmx200m
     * -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./heapdump.hprof -XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:./gc.log
     * -jar springboot-oom-1.0-SNAPSHOT.jar
     *
     * ab工具压测命令:
     * ab -n 10000  -c 10 'http://127.0.0.1:8888/memoryLeak'
     *
     */
    @RequestMapping(value = "/memoryLeak")
    public String memoryLeakTest(){
        Long uid = Long.valueOf(count.addAndGet(1L));
        UserInfoPO userInfoPO = UserInfoPO.builder()
                .uid(uid)
                .name("测试姓名")
                .idNo("测试身份证号")
                .phone("测试手机号")
                .address("测试住址")
                .company("测试工作单位")
                .build();

        map.put(uid,userInfoPO);
        return ResultConstants.SUCCESS;
    }
}
