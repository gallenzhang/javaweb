package com.gallenzhang.oom.service;
import org.springframework.stereotype.Service;

/**
 * @author : zhangxq
 * @date : 2019/8/7
 * @description :
 */
@Service
public class VMStackOverflowService {
    private int stackLength = 1;
    public int getStackLength() {
        return stackLength;
    }

    public void stackLeak(){
        stackLength ++;
        stackLeak();
    }
}
