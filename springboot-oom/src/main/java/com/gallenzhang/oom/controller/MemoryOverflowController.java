package com.gallenzhang.oom.controller;

import com.gallenzhang.oom.constant.ResultConstants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : zhangxq
 * @date : 2019/08/06
 * @description : 内存溢出示例
 */
@RestController
public class MemoryOverflowController {

    /**
     *
     * 启动命令:
     * java -Xms100M -Xmx200M
     * -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./heapdump.hprof -XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:./gc.log
     * -jar  springboot-oom-1.0-SNAPSHOT.jar
     *
     *
     * ab工具压测命令:
     * ab -n 10000  -c 10 'http://127.0.0.1:8888/memoryOverflow'
     *
     */
    @RequestMapping(value = "/memoryOverflow")
    public String memoryOverflowTest(){
        List<Byte[]> list = new ArrayList<>();

        Byte[] b = new Byte[1024 * 1024];
        list.add(b);

        return ResultConstants.SUCCESS;
    }
}
