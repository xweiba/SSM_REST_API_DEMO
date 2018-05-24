package com.jnshu.tools;

import redis.clients.jedis.Jedis;

/**
 * @program: taskTwo
 * @description: Redis 测试类
 * @author: Mr.xweiba
 * @create: 2018-05-23 10:00
 **/

public class RedisClient {
    public static void main(String[] args) {
        // 连接Redis
        Jedis jedis = new Jedis("localhost");
        System.out.println("连接成功");
        //查看服务是否运行
        System.out.println("服务正在运行: "+jedis.ping());
        System.out.println(jedis.get("liuhuan"));
        Long times = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            jedis.set(String.valueOf(i), String.valueOf(i));
        }
        Long s = (System.currentTimeMillis()-times)/1000;
        System.out.println("插入10w条数据:" + s);
    }
}
