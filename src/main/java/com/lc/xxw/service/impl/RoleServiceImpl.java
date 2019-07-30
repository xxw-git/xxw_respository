package com.lc.xxw.service.impl;

import com.google.common.collect.Lists;
import com.lc.xxw.common.utils.CommonUtils;
import com.lc.xxw.entity.Role;
import com.lc.xxw.entity.UserRoles;
import com.lc.xxw.mapper.RoleMapper;
import com.lc.xxw.mapper.UserRoleMapper;
import com.lc.xxw.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 *@description: 角色管理实现类
 *@className: RoleServiceImpl
 *@author: xuexiaowei
 *@create: 2019-07-27 09:42
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<Role> selectAll(){
        return roleMapper.selectAll();
    }

    /**
     * 保存到用户角色中间表
     * @param roleId 角色id
     * @param userId  用户id
     */
    @Override
    public void insertUserRole(String roleId,String userId){
        UserRoles userRoles = new UserRoles();
        userRoles.setUserId(userId);
        List<String> list = selectRolesByUserId(userId);
        if(!CommonUtils.isEmpty(list)){
            userRoleMapper.delete(userRoles);
        }
        userRoles.setId(CommonUtils.getUUID());
        userRoles.setRoleId(roleId);
        userRoleMapper.insertSelective(userRoles);
    }

    /**
     * 根据用户id，查询角色
     * @param userId
     * @return
     */
    @Override
    public List<String> selectRolesByUserId(String userId){
        List<String> roleIds = Lists.newArrayList();
        Example example = new Example(UserRoles.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        example.orderBy("roleId").asc();
        List<UserRoles> userRoles = userRoleMapper.selectByExample(example);
        for(UserRoles userRole : userRoles){
            roleIds.add(userRole.getRoleId());
        }
        return roleIds;
    }


}
