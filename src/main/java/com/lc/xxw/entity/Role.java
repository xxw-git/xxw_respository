package com.lc.xxw.entity;

import com.lc.xxw.common.enmus.StatusEnum;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @description: 角色表
 * @author: xuexiaowei
 * @create: 2019-07-27 08:46
 */
@Table(name = "SYS_ROLE")
@Data
public class Role implements Serializable {

    /** 主键 */
    @Id
    private String id;

    /** 角色名称 */
    private String name;

    /** 角色描述 */
    private String description;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /** 状态 */
    private Byte status = StatusEnum.OK.getCode();

    /** 备注 */
    private String remark;

}
