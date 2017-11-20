package com.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.seckill.entity.SecKill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;

    public RedisDao(String ip,int port) {
        this.jedisPool = new JedisPool(ip,port);
    }

    private RuntimeSchema<SecKill> schema = RuntimeSchema.createFrom(SecKill.class);

    public SecKill getSeckill(long seckillId){
        //redis操作逻辑
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckillId;

                //自动以序列化
                byte[] bytes = jedis.get(key.getBytes());
                //缓存中获取到bytes
                if(bytes != null){
                    //空对象
                    SecKill secKill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes,secKill,schema);
                    return secKill;
                }
            }finally {
                jedis.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public String putSeckill(SecKill secKill){
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + secKill.getSeckillId();
                byte[]bytes = ProtostuffIOUtil.toByteArray(secKill,schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //设置超时时间
                int timeout = 60 * 60;  //1小时
                String result = jedis.setex(key.getBytes(),timeout,bytes);
                return result;
            }finally {
                jedis.close();
            }
        }catch (Exception e){
           logger.error(e.getMessage(),e);
        }
        return null;
    }
}
