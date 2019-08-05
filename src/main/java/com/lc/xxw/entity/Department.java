package com.lc.xxw.entity;

import com.lc.xxw.common.enmus.StatusEnum;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description: 部门表
 * @className: Department
 * @author: xuexiaowei
 * @create: 2019-08-02 10:16
 */
@Data
@Table(name = "SYS_DEPARTMENT")
public class Department implements Serializable {

    /** 主键 */
    @Id
    private String id;

    /** 部门名称 */
    private String name;

    /** 父id */
    @Column(name = "pid")
    private String pId;

    /** 部门全称 */
    private String fullName;

    /** 图标 */
    private String icon;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /** 状态 */
    private Byte status = StatusEnum.OK.getCode();

    /** 备注 */
    private String remark;

    @Transient
    private List<Department> children;

}
