package com.lc.xxw.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lc.xxw.common.enmus.StatusEnum;
import com.lc.xxw.common.utils.CommonUtils;
import com.lc.xxw.entity.PageValid;
import com.lc.xxw.entity.User;
import com.lc.xxw.mapper.UserMapper;
import com.lc.xxw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 分页查询
     * @param page
     * @return
     */
    public PageInfo<User> findByPage(PageValid page){
        PageInfo<User> pageInfo = PageHelper.startPage(page.getCurrentPage(),page.getPageSize()).doSelectPageInfo(
                new ISelect() {
                    @Override
                    public void doSelect() {
                        selectAll();
                    }
                }
        );
        return pageInfo;
    }

    public List<User> selectAll() {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("status",StatusEnum.DELETE.getCode());
        example.orderBy("createTime").desc();
        return userMapper.selectByExample(example);
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
        userId = userId == null ? String.valueOf(Long.MIN_VALUE) : userId;
        List<User> list = userMapper.repeatByUserName(userId,account);
        if(CommonUtils.isEmpty(list)){
            return false;
        }else {
            return true;
        }
    }

    public JSONObject save(User user){
        JSONObject result = new JSONObject();

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

    /**
     * 批量删除
     * @param ids 用户id
     * @param status
     */
    @Override
    public void updateStatus(String[] ids, Byte status){
        for (String id: ids) {
            User user = new User();
            user.setId(id);
            user.setStatus(status);
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    @Override
    public User selectUserByPk(String id){
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(User user){
        return userMapper.updateByPrimaryKeySelective(user);
    }

}
