package com.lc.xxw.service;

import com.lc.xxw.entity.Role;

import java.util.List;

/**
 *@description: 角色管理Service
 *@className: RoleService
 *@author: xuexiaowei
 *@create: 2019-07-27 09:42
 */
public interface RoleService {
    /**
     * 查询所有角色
     * @return
     */
    List<Role> selectAll();

    /**
     * 保存到用户角色中间表
     * @param roleId 角色id
     * @param userId  用户id
     */
    void insertUserRole(String roleId,String userId);

    /**
     * 根据用户id，查询角色
     * @param userId
     * @return
     */
    List<String> selectRolesByUserId(String userId);
}
