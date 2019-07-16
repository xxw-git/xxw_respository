package com.lc.xxw.mapper;

import com.lc.xxw.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

@Repository
public interface UserMapper extends Mapper<User> {

    /**
     * 根据用户id 查询角色信息
     * @param userId
     * @return
     */
    Set<String> findRolesByUserId(String userId);

    /**
     * 根据用户id 查询权限信息
     * @param userId
     * @return
     */
    Set<String> findPermissionsByUserId(String userId);

    /**
     * 查询用户账户是否重复
     * @param userId
     * @param account
     * @return
     */
    List<User> repeatByUserName(@Param("userId") String userId, @Param("account") String account);

}
