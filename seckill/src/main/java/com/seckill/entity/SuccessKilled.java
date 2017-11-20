package com.seckill.entity;

import java.util.Date;

public class SuccessKilled {

    private long seckilled;

    private long userphone;

    private short state;

    private Date createTime;

    //多对一
    private SecKill secKill;

    public long getSeckilled() {
        return seckilled;
    }

    public void setSeckilled(long seckilled) {
        this.seckilled = seckilled;
    }

    public long getUserphone() {
        return userphone;
    }

    public void setUserphone(long userphone) {
        this.userphone = userphone;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public SecKill getSecKill() {
        return secKill;
    }

    public void setSecKill(SecKill secKill) {
        this.secKill = secKill;
    }

    @Override
    public String toString() {
        return "SuccessKilled{" +
                "seckilled=" + seckilled +
                ", userphone=" + userphone +
                ", state=" + state +
                ", createTime=" + createTime +
                '}';
    }
}
