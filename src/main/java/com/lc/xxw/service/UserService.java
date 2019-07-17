package com.lc.xxw.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.lc.xxw.entity.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @return
     */
    PageInfo<User> findByPage(int currentPage, int pageSize);
    /**
     * 查询所有人员
     * @return
     */
    List<User> selectAll();

    /**
     * 根据用户Id查找角色
     * @param userId
     * @return
     */
    Set<String> findRolesByUserId(String userId);

    /**
     * 根据用户Id查找拥有的权限
     * @param UserId
     * @return
     */
    Set<String> findPermissions(String UserId);

    /**
     * 根据用户账号查询用户实体
     * @param account
     * @return
     */
    User findUserByUsername(String account);

    User login(String account,String password);

    /**
     * 禁用账号
     * @param id
     * @param status
     * @return
     */
    int updateDisabled(String id,Byte status);

    /**
     * 保存用户信息
     * @param user
     * @return
     */
    JSONObject save(User user);

    /**
     * 判断用户是否重复
     * @param userId
     * @param account
     * @return
     */
    Boolean repeatByUserName(String userId,String account);

}
