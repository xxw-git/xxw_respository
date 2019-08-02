package com.lc.xxw.entity;

import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * @description: 部门表
 * @className: Department
 * @author: xuexiaowei
 * @create: 2019-08-02 10:16
 */
@Data
@Table(name = "SYS_DEPARTMENT")
public class Department extends BaseEntity {

    /** 部门名称 */
    private String deptName;

    /** 父id */
    private String parentId;

    @Transient
    private List<Department> children;

}
