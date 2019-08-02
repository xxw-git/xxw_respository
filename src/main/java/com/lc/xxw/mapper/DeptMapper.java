package com.lc.xxw.mapper;

import com.lc.xxw.entity.Department;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @description: 部门mapper
 * @className: DeptMapper
 * @author: xuexiaowei
 * @create: 2019-08-02 10:45
 */
@Repository
public interface DeptMapper extends Mapper<Department> {
}
