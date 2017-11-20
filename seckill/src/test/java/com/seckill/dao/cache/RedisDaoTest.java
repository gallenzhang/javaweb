package com.seckill.dao.cache;

import com.seckill.dao.SecKillDao;
import com.seckill.dto.SeckillResult;
import com.seckill.entity.SecKill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {

    private long id = 1001;

    @Autowired
    private SecKillDao secKillDao;

    @Autowired
    private RedisDao redisDao;

    @Test
    public void testSeckill() throws Exception {
        //get and put
        SecKill secKill = redisDao.getSeckill(id);
        if(secKill == null){
            secKill = secKillDao.queryById(id);
            if(secKill != null){
                String result = redisDao.putSeckill(secKill);
                System.out.println(result);
                secKill = redisDao.getSeckill(id);
                System.out.println(secKill);
            }
        }
    }


}