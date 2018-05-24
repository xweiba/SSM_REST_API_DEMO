package com.jnshu.controller;

import com.jnshu.model.UserCustom;
import com.jnshu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

/**
 * @program: taskTwo
 * @description: Redis 缓存接口
 * @author: Mr.xweiba
 * @create: 2018-05-23 17:30
 **/
@Controller
@RequestMapping(value = "/redis")
public class RedisController {
    @Autowired
    UserService userService;

    // 原生接口性能测试
    @RequestMapping(value = "/gety", method = RequestMethod.GET)
    @ResponseBody
    public String getyRedis() throws Exception {
        Jedis jedis = new Jedis("localhost");
        return jedis.get("user1");
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public UserCustom getRedis() throws Exception {
        return userService.getRedis("user1");
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ResponseBody
    public void addRedis() throws Exception{
        UserCustom userCustom = new UserCustom();
        userCustom.setId(1);
        userCustom.setUsername("liuhaun");
        userService.addRedis(userCustom);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public void deleteRedis(String key) throws Exception {
        userService.delRedis(key);
    }
}
