package com.lc.xxw.entity;

import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name="SYS_USER")
@Data
public class User extends BaseEntity {

    /** 登录账号 */
    private String loginAccount;

    /** 用户姓名 */
    private String userName;

    /** 密码 */
    private String password;

    /** 身份证号 */
    private String sfzh;

    /** 邮箱 */
    private String email;

    /** 联系方式 */
    private String lxfs;

    /** 部门id */
    private String deptId;

    @Transient
    private String roleIds;

}
