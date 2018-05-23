package com.jnshu.service.impl;

import com.jnshu.mapper.AuthDao;
import com.jnshu.mapper.UserAuthDao;
import com.jnshu.mapper.UserDao;
import com.jnshu.model.*;
import com.jnshu.service.UserService;
import com.jnshu.tools.MemcacheUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("userServiceImpl")
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    AuthDao authDao;
    @Autowired
    UserAuthDao userAuthDao;

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public List<UserCustom> findUserMore(UserQV userQV) throws Exception {
        logger.info("传入 userQv: " + userQV.toString());
        // 复杂查询 每次数据都不同 不能做缓存 当查询不为空时 执行
        if(userQV.getUserCustom() != null){
            logger.info("复杂查询开始");
            return userDao.findUserMore(userQV);
        }

        UserList userList = new UserList();
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
        Boolean flag = userDao.deleteUser(i);
        // 操作数据后 删除 查询所有信息 的缓存
        if(flag){
            logger.info("userAll is delete");
            MemcacheUtils.delete("userAll");
        }
        return flag;
    }

    /* Session 验证 */
    @Override
    public boolean findAuth(Auth auth) throws Exception {
        // 密码验证的就不做缓存了
        return authDao.findAuth(auth);
    }

    /* Cookie 验证 */
    @Override
    public boolean userAuth(UserAuth userAuth) {
        return userAuthDao.userAuth(userAuth);
    }
    @Override
    public UserAuth findUserAuthByName(String au_username) {
        return userAuthDao.findUserAuthbyName(au_username);
    }

    @Override
    public Boolean findUserAuthByid(Integer id) {
        return userAuthDao.findUserAuthByid(id);
    }

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void addRedis(UserCustom userCustom) throws Exception {
        ValueOperations<String, UserCustom> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(String.valueOf(userCustom.getId()), userCustom);
    }

    @Override
    public UserCustom getRedis(String key) throws Exception {
        ValueOperations<String, UserCustom> valueOperations = redisTemplate.opsForValue();
        UserCustom userCustom = valueOperations.get(key);
        return userCustom;
    }
}
