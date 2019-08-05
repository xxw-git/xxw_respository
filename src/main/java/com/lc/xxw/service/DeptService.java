package com.lc.xxw.service;

import com.lc.xxw.entity.Department;

import java.util.List;

/**
 * @description: 部门Service
 * @className: DeptService
 * @author: xuexiaowei
 * @create: 2019-08-02 10:40
 */
public interface DeptService {

    List<Department> selectByDept(Department dept);

    void save(Department dept);

    Boolean isExistDeptName(Department dept);

    void deleteByPk(Byte status,String id);
}
