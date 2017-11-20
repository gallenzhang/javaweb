package com.seckill.service.impl;

import com.seckill.dao.SecKillDao;
import com.seckill.dao.SuccessKilledDao;
import com.seckill.dao.cache.RedisDao;
import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.SecKill;
import com.seckill.entity.SuccessKilled;
import com.seckill.enums.SeckillStatEnum;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;
import com.seckill.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SecKillDao secKillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    //md5盐值字符串，用于混淆MD5
    private final String slat = "sadfdlasjf233)*)(*)*^*%%&HKHKHdhkaskhdaskhdaskdh!~!@";

    public List<SecKill> getSeckillList() {
        return secKillDao.queryAll(0,4);
    }

    public SecKill getById(long seckillId) {
        return secKillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        //优化点1：缓存优化：超时的基础上维护一致性
        //1.访问redis
        SecKill secKill = redisDao.getSeckill(seckillId);
        if(secKill == null){
            //2.方位数据库
            secKill = secKillDao.queryById(seckillId);
            if(secKill == null){
                return new Exposer(false,seckillId);
            }else{
                //3.放入redis
                redisDao.putSeckill(secKill);
            }
        }

        Date startTime = secKill.getStartTime();
        Date endTime = secKill.getEndTime();
        //系统当前时间
        Date noewTime = new Date();
        if(noewTime.getTime() < startTime.getTime() || noewTime.getTime() > endTime.getTime()){
            return new Exposer(false,seckillId,noewTime.getTime(),startTime.getTime(),endTime.getTime());
        }

        //转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    private String getMD5(long seckillId){
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Transactional
    /**
     * 使用注解控制事务方法的优点：
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格。
     * 2.保证事务方法执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部
     * 3.不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5 == null || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }

        //执行秒杀逻辑：减库存 + 记录购买行为
        Date nowTime = new Date();

        try{
            /**
             *
             *
             //减库存
             int updateCount = secKillDao.reduceNumber(seckillId,nowTime);
             if(updateCount <= 0){
                throw new SeckillCloseException("seckill is closed");
             }else{
                 //记录购买行为
                 int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);

                 //唯一：seckillId,userPhone
                 if(insertCount <= 0){
                 throw new RepeatKillException("seckill repeated");
             }else{
                 //秒杀成功
                 SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                 return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS,successKilled);
                 }
             }
             */

            //优化2：将insert语句提前，可以减少一部分重复秒杀操作，降低了操作SQL消耗的网络延迟以及减少了GC停顿时间

            //记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);

            //唯一：seckillId,userPhone
            if(insertCount <= 0){
                //重复秒杀
                throw new RepeatKillException("seckill repeated");
            }else{
                //减库存，热点商品竞争
                int updateCount = secKillDao.reduceNumber(seckillId,nowTime);
                if(updateCount <= 0){
                    //没有更新到记录，秒杀结束，rollback
                    throw new SeckillCloseException("seckill is closed");
                }else{
                    //秒杀成功 commit
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS,successKilled);
                }
            }

        }catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatKillException e2){
            throw e2;
        }catch (Exception e){
            logger.error(e.getMessage(),e);

            //所有编译期异常转化为运行期异常
            throw new SeckillException("seckill inner error:" + e.getMessage());

        }
    }

    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
        if(md5 == null || !md5.equals(getMD5(seckillId))){
            return new SeckillExecution(seckillId,SeckillStatEnum.DATA_REWRITE);
        }

        Date killTime = new Date();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("seckillId",seckillId);
        map.put("phone",userPhone);
        map.put("killTime",killTime);
        map.put("result",null);

        //执行存储过程，result被赋值
        try {
            secKillDao.killByProcedure(map);
            //获取result
            int result = MapUtils.getInteger(map,"result",-2);
            if(result == 1){
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                return new SeckillExecution(seckillId,SeckillStatEnum.SUCCESS,successKilled);
            }else{
                return new SeckillExecution(seckillId,SeckillStatEnum.stateOf(result));
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new SeckillExecution(seckillId,SeckillStatEnum.INNER_ERROR);
        }
    }
}
