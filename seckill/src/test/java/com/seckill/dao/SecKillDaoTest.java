package com.seckill.dao;

import com.seckill.entity.SecKill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;


/**
 * 配置Spring和Junit整合，Junit启动时加载SpringIOC容器
 * spring-test,junit
 */

@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit Spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SecKillDaoTest {

    //注入Dao实现类依赖
    @Resource
    private SecKillDao secKillDao;

    @Test
    public void queryById() throws Exception {
        long id = 1000;
        SecKill secKill = secKillDao.queryById(id);
        System.out.println(secKill.getName());
        System.out.println(secKill);
    }

    @Test
    public void queryAll() throws Exception {
        List<SecKill> secKills = secKillDao.queryAll(0,100);
        for(SecKill secKill:secKills){
            System.out.println(secKill);
        }
    }

    @Test
    public void reduceNumber() throws Exception {
        Date killTime = new Date();
        int updateCount = secKillDao.reduceNumber(1000L,killTime);
        System.out.println("updateCount=" + updateCount);
    }



}