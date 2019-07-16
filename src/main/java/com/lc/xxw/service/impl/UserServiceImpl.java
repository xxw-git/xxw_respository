package com.lc.xxw.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lc.xxw.common.utils.CommonUtils;
import com.lc.xxw.entity.User;
import com.lc.xxw.mapper.UserMapper;
import com.lc.xxw.service.UserService;
import com.lc.xxw.shiro.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> selectAll() {
        return userMapper.selectAll();
    }

    public Set<String> findRolesByUserId(String userId){
        return userMapper.findRolesByUserId(userId);
    }

    public Set<String> findPermissions(String userId){
        return userMapper.findPermissionsByUserId(userId);
    }

    public User findUserByUsername(String account){
        User user = new User();
        user.setLoginAccount(account);
        return userMapper.selectOne(user);
    }

    public User login(String account,String password){
        User user = new User();
        user.setLoginAccount(account);
        user.setPassword(password);
        return userMapper.selectOne(user);
    }

    public int updateDisabled(String id,Byte status){
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        return userMapper.updateByPrimaryKey(user);
    }

    public Boolean repeatByUserName(String userId,String account){
        List<User> list = userMapper.repeatByUserName(userId,account);
        if(CommonUtils.isEmpty(list)){
            return false;
        }else {
            return true;
        }
    }

    public JSONObject save(User user){
        JSONObject result = new JSONObject();
        Boolean isRepeat = repeatByUserName(user.getId(),user.getLoginAccount());
        if(isRepeat){
            result.put("status",500);
            result.put("message","用户账号已存在。");
            return result;
        }
        if(CommonUtils.isEmpty(user.getId())){
            user.setId(CommonUtils.getUUID());
            user.setCreateTime(new Date());
            userMapper.insertSelective(user);
            result.put("status",200);
            result.put("message","保存成功。");
        } else {
            user.setUpdateTime(new Date());
            userMapper.updateByPrimaryKeySelective(user);
            result.put("status",200);
            result.put("message","保存成功。");
        }
        return result;
    }

}
