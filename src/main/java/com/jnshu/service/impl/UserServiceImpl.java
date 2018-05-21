package com.jnshu.service.impl;

import com.jnshu.mapper.AuthDao;
import com.jnshu.mapper.UserDao;
import com.jnshu.model.*;
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
        UserList userList = new UserList();
        // 查找缓存
        Object object = MemcacheUtils.get("userAll");
        // 当缓存不为空时 直接返回缓存
        if (object != null) {
            logger.debug("userALl 缓存输出 ");
            userList = (UserList) object;
            // 直接返回缓存
            return userList.getUserList();
        }
        List<UserCustom> userCustomList = userDao.findUserMore(userQV);
        // UserList userList = new UserList();
        userList.setUserList(userCustomList);
        // 当缓存为空时 添加 memcached 缓存
        logger.debug("userALl 设置缓存");
        MemcacheUtils.set("userAll", userList);
        return userCustomList;
    }

    @Override
    public UserCustom findUserById(Integer id) throws Exception {
        // 查找缓存
        logger.debug("查询缓存中");
        Object object = MemcacheUtils.get("user" + id);
        logger.debug("缓存查询完成");
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
        Boolean flag = MemcacheUtils.add("user" + user.getId(), user);
        // 操作数据后 删除 查询所有信息 的缓存
        if(flag){
            MemcacheUtils.delete("userAll");
        }
        //所以返回user的id值
        return user.getId();
    }

    @Override
    public boolean updateUser(UserCustom userCustom, Integer id) throws Exception {
        userCustom.setId(id);
        // 写入缓存 这里使用replace, 当key(id)不存在时, 不写入缓存
        Boolean flag = MemcacheUtils.replace("user" + id, userCustom);
        // 操作数据后 删除 查询所有信息 的缓存
        if(flag){
            logger.info("userAll is delete");
            MemcacheUtils.delete("userAll");
        }
        return userDao.updateUser(userCustom);
    }

    @Override
    public boolean deleteUser(Integer i) throws Exception {
        // 删除缓存
        Boolean flag = MemcacheUtils.delete(String.valueOf(i));
        // 操作数据后 删除 查询所有信息 的缓存
        if(flag){
            logger.info("userAll is delete");
            MemcacheUtils.delete("userAll");
        }
        return userDao.deleteUser(i);
    }

    @Override
    public boolean findAuth(Auth auth) throws Exception {
        // 密码验证的就不做缓存了
        return authDao.findAuth(auth);
    }
}
