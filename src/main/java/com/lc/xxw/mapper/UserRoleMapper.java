package com.lc.xxw.mapper;

import com.lc.xxw.entity.UserRoles;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @description: 用户角色Mapper
 * @className: UserRoleMapper
 * @author: xuexiaowei
 * @create: 2019-07-28 10:57
 */
@Repository
public interface UserRoleMapper extends Mapper<UserRoles> {
}
