package com.jnshu.controller;

import com.jnshu.model.UserCustom;
import com.jnshu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public UserCustom getRedis() throws Exception {
        return userService.getRedis("1");
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ResponseBody
    public void addRedis() throws Exception{
        UserCustom userCustom = new UserCustom();
        userCustom.setId(1);
        userCustom.setUsername("liuhaun");
        userService.addRedis(userCustom);
    }
}