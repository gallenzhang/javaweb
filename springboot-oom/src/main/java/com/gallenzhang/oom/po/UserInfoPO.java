package com.gallenzhang.oom.po;

import lombok.Builder;
import lombok.Data;

/**
 * @author : zhangxq
 * @date : 2019/08/06
 * @description :用户信息PO
 */
@Data
@Builder
public class UserInfoPO {

    private Long uid;

    /**
     * 身份证号
     */
    private String idNo;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 住址
     */
    private String address;

    /**
     * 工作单位
     */
    private String company;

    /**
     * 1M 大小的字节数组
     */
    private Byte[] bytes = new Byte[1024 * 1024];
}
