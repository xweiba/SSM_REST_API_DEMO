package com.jnshu.tools;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.TimeUnit;

/**
 * @program: taskTwo
 * @description: Redis 缓存工具类
 * @author: Mr.xweiba
 * @create: 2018-05-23 23:24
 * @des: https://blog.csdn.net/qq_34021712/article/details/75949706
 **/

public class RedisUtils {
    private RedisTemplate<String, Object> redisTemplate;

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * @Description: 设置指定key的过期时间
     * @Param: [key, time] key名, 过期时间(s)
     * @return: boolean
     * @Author: Mr.Wang
     * @Date: 2018/5/24
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                // TimeUnit.SECONDS 颗粒度按秒计算
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @Description: 获取指定key的获取时间
     * @Param: [key]
     * @return: long 颗粒度为秒
     * @Author: Mr.Wang
     * @Date: 2018/5/24
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * @Description: 判断key值是否存在
     * @Param: [key]
     * @return: boolean
     * @Author: Mr.Wang
     * @Date: 2018/5/24
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @Description: 删除指定 key, 或key集合
     * @Param: [key]
     * @return: void
     * @Author: Mr.Wang
     * @Date: 2018/5/24
     */
    public void delKey(String... key) {
        if (key.length == 1) {
            redisTemplate.delete(key[0]);
        } else {
            // CollectionUtils.arrayToList(key) 将key字符串集合转换为list对象集合
            redisTemplate.delete(CollectionUtils.arrayToList(key));
        }
    }

    /**
     * @Description: 获取指定key的value
     * @Param: [key]
     * @return: java.lang.Object
     * @Author: Mr.Wang
     * @Date: 2018/5/24
     */
    public Object get(String key) {
        // 三元运算符 key为空返回null,否则返回redisTemplate.opsForValue().get(key);
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * @Description: 添加普通缓存
     * @Param: [key, object]
     * @return: boolean
     * @Author: Mr.Wang
     * @Date: 2018/5/24
     */
    public boolean set(String key, Object object) {
        try {
            redisTemplate.opsForValue().set(key, object);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    
    /**
     * @description 添加普通缓存并设置过期时间
     * @param: [key] 键
     * @param: [object] 值
     * @param: [time] 过期时间, 单位秒, 如果time小于等于0 将设置无限期
     * @return: boolean
     * @author: Mr.xweiba
     * @date: 2018/5/24 16:07
     * @since: 1.0.0
     */
    public boolean set(String key, Object object, Long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, object, time, TimeUnit.SECONDS);
            } else {
                set(key, object);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @Description: 添加缓存
     * @Param: [userCustom]
     * @return: boolean
     * @Author: Mr.Wang
     * @Date: 2018/5/23
     */
   /* public void addRedis(UserCustom userCustom){
        valueOperations = redisTemplate.opsForValue();
        // 设置值 以id为key
        valueOperations.set("user" + String.valueOf(userCustom.getId()), userCustom);
    }

    public UserCustom getRedis(String key){
        valueOperations = redisTemplate.opsForValue();
        // 删除 key
        return valueOperations.get(key);
    }
    public void delRedis(String key){
        // 删除
        redisTemplate.delete(key);
    }*/
}
