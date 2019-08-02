package com.lc.xxw.service.impl;

import com.lc.xxw.common.utils.StringUtils;
import com.lc.xxw.entity.Department;
import com.lc.xxw.mapper.DeptMapper;
import com.lc.xxw.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @description: 部门Service实现类
 * @className: DeptServiceImpl
 * @author: xuexiaowei
 * @create: 2019-08-02 10:43
 */
@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<Department> selectByDept(Department dept){
        Example example = new Example(Department.class);
        if(null != dept){
            Example.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotEmpty(dept.getId())){
                criteria.andEqualTo("id",dept.getId());
            }
            if(StringUtils.isNotEmpty(dept.getParentId())){
                criteria.andEqualTo("parentId",dept.getParentId());
            }
            if(StringUtils.isNotEmpty(dept.getDeptName())){
                criteria.andLike("deptName",dept.getDeptName());
            }
            if(null != dept.getStatus()){
                criteria.andEqualTo("status",dept.getStatus());
            }
        }
        example.orderBy("createTime").desc();
        return deptMapper.selectByExample(example);
    }

    @Override
    public void insert(Department dept){
        deptMapper.insertSelective(dept);
    }
}
