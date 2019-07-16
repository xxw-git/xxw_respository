package com.lc.xxw;

import com.lc.xxw.common.utils.RedisUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

/**
 * @description: 测试redis连接
 * @author: xuexiaowei
 * @create: 2019-07-13 13:13
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/applicationContext.xml",
        "classpath:spring-redis.xml"})
public class TestJedisClient {

    private static final Logger log = LogManager.getLogger(TestJedisClient.class);

   @Autowired
   private JedisConnectionFactory jedisConnectionFactory;

   private static Jedis jedis;

   @Autowired
   private RedisUtils redisUtils;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testJedisClient(){
        try{
            if(jedis == null){
                jedis = (Jedis)jedisConnectionFactory.getConnection().getNativeConnection();
            }
            jedis.set("test003","123");
            log.info("test003的值是" + jedis.get("test003"));
            jedis.del("test003");
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            jedis.close();
        }

    }

    @Test
    public void test(){
        redisUtils.set("testRedisTemplate","123456");
        log.info(redisUtils.get("testRedisTemplate"));
        redisUtils.delete("testRedisTemplate");
    }

}
