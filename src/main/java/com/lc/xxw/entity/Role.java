package com.lc.xxw.entity;

import lombok.Data;

import javax.persistence.Table;

/**
 * @description: 角色表
 * @author: xuexiaowei
 * @create: 2019-07-27 08:46
 */
@Table(name = "SYS_ROLE")
@Data
public class Role extends BaseEntity {

    private String name;

    private String description;

}
