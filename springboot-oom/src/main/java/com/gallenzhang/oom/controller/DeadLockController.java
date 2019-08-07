package com.gallenzhang.oom.controller;

import com.gallenzhang.oom.constant.ResultConstants;
import com.gallenzhang.oom.thread.DeadThreadA;
import com.gallenzhang.oom.thread.DeadThreadB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : zhangxq
 * @date : 2019/08/06
 * @description : 死锁示例
 */
@RestController
public class DeadLockController {
    private Logger logger = LoggerFactory.getLogger(DeadLockController.class);
    private static Object lockA = new Object();
    private static Object lockB = new Object();


    @RequestMapping(value = "/deadLock")
    public String deadLockTest(){
        Thread threadA = new Thread(new DeadThreadA(lockA,lockB),"Thread-A");
        Thread threadB = new Thread(new DeadThreadB(lockA,lockB),"Thread-B");
        threadA.start();
        threadB.start();
        try {
            threadA.join();
            threadB.join();
        } catch (InterruptedException e) {
            logger.error("线程{} 发生中断异常 !",Thread.currentThread().getName(),e);
            e.printStackTrace();
        }
        return ResultConstants.SUCCESS;
    }

}
