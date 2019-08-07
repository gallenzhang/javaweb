package com.gallenzhang.oom.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : zhangxq
 * @date : 2019/8/7
 * @description : 死锁线程A
 */
public class DeadThreadA implements Runnable  {
    private Logger logger = LoggerFactory.getLogger(DeadThreadA.class);

    private Object lockA;
    private Object lockB;

    public DeadThreadA(Object lockA,Object lockB){
        this.lockA = lockA;
        this.lockB = lockB;
    }


    @Override
    public void run() {
        synchronized (lockA){
            try {
                logger.info("线程{} 成功获取lockA !",Thread.currentThread().getName());
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                logger.error("线程{}发生中断异常 !",Thread.currentThread().getName(),e);
            }
            synchronized (lockB){
                logger.info("线程{} 成功获取lockB !",Thread.currentThread().getName());
            }
        }
    }
}
