package com.lc.xxw.entity;

import com.lc.xxw.common.enmus.StatusEnum;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name="SYS_USER")
@Data
public class User implements Serializable {

    private static final Long serializableId = 1L ;

    @Id
    private String id;

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

    /** 状态 */
    private Byte status = StatusEnum.OK.getCode();

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;


}
