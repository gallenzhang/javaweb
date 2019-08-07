package com.gallenzhang.oom.controller;

import com.gallenzhang.oom.service.VMStackOverflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : zhangxq
 * @date : 2019/08/06
 * @description : 虚拟机栈溢出示例
 */
@RestController
public class VMStackOverflowController {
    @Autowired
    private VMStackOverflowService vmStackOverflowService;
}
