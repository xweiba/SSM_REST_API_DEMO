package com.jnshu.service.impl;

import com.jnshu.controller.LoginController;
import com.jnshu.mapper.AuthDao;
import com.jnshu.mapper.UserDao;
import com.jnshu.model.Auth;
import com.jnshu.model.User;
import com.jnshu.model.UserCustom;
import com.jnshu.model.UserQV;
import com.jnshu.service.UserService;
import com.jnshu.tools.MemcacheUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("userServiceImpl")
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    AuthDao authDao;

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public List<UserCustom> findUserMore(UserQV userQV) throws Exception {
        // 查找缓存
        Object object = MemcacheUtils.get("userALl");
        // 当缓存不为空时 直接返回缓存
        if (object != null) {
            logger.debug("userALl 缓存输出 ");
            // 直接返回缓存
            return (List<UserCustom>) object;
        }
        List<UserCustom> userCustomList = userDao.findUserMore(userQV);
        // 当缓存为空时 添加 memcached 缓存
        logger.debug("userALl 设置缓存");
        MemcacheUtils.set("userAll", userCustomList.toArray());
        return userCustomList;
    }

    @Override
    public UserCustom findUserById(Integer id) throws Exception {
        // 查找缓存
        Object object = MemcacheUtils.get("user" + id);
        // 当存在缓存时直接返回缓存数据
        if (object != null) {
            return (UserCustom) object;
        }
        UserCustom userCustom = userDao.findUserById(id);
        // 当缓存为空时 添加 memcached 缓存
        MemcacheUtils.set("user" + id, userCustom);
        return userCustom;
    }

    @Override
    public int insertUser(User user) throws Exception {
        //插入成功后返回的值存入了user的id中
        userDao.insertUser(user);
        // 写入缓存 这里使用add 当 key(id)存在时, 不写入缓存
        MemcacheUtils.add("user" + user.getId(), user);
        //所以返回user的id值
        return user.getId();
    }

    @Override
    public boolean updateUser(UserCustom userCustom, Integer id) throws Exception {
        userCustom.setId(id);
        // 写入缓存 这里使用replace, 当key(id)不存在时, 不写入缓存
        MemcacheUtils.replace("user" + id, userCustom);
        return userDao.updateUser(userCustom);
    }

    @Override
    public boolean deleteUser(Integer i) throws Exception {
        // 删除缓存
        MemcacheUtils.delete(String.valueOf(i));
        return userDao.deleteUser(i);
    }

    @Override
    public boolean findAuth(Auth auth) throws Exception {
        // 密码验证的就不做缓存了
        return authDao.findAuth(auth);
    }
}
