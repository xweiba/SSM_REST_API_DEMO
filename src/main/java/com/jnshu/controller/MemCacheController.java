package com.jnshu.controller;

import com.jnshu.tools.MemcacheUtils;
import com.whalin.MemCached.MemCachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @program: taskTwo
 * @description: MemCache 测试类
 * @author: Mr.xweiba
 * @create: 2018-05-19 00:06
 **/

@Controller
public class MemCacheController {
    private static Logger logger = LoggerFactory.getLogger(MemCacheController.class);

    @RequestMapping(value = "/findSessionByKey", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object findByKey(@RequestBody String key){
        System.out.println(" MemcacheUtils 测试: " + key );
        logger.info("MemCacheController.findByKey param:key="+key);
        if(StringUtils.isEmpty(key)){
            return "key must not be empty or null!";
        }
        return MemcacheUtils.get(key);
    }
}
