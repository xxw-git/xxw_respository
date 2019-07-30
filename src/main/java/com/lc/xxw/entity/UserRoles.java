package com.lc.xxw.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @description: 用户角色关联表
 * @author: xuexiaowei
 * @create: 2019-07-27 08:54
 */
@Table(name = "SYS_USER_ROLES")
@Data
public class UserRoles implements Serializable {

    @Id
    private String id;

    private String userId;

    private String roleId;
}
