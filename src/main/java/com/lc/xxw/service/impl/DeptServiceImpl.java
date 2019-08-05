package com.lc.xxw.service.impl;

import com.lc.xxw.common.enmus.StatusEnum;
import com.lc.xxw.common.utils.CommonUtils;
import com.lc.xxw.common.utils.StringUtils;
import com.lc.xxw.constants.IconConstants;
import com.lc.xxw.constants.StatusConstants;
import com.lc.xxw.entity.Department;
import com.lc.xxw.mapper.DeptMapper;
import com.lc.xxw.service.DeptService;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
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
        Example.Criteria criteria = example.createCriteria();
        if(null != dept){
            if (StringUtils.isNotEmpty(dept.getId())){
                criteria.andEqualTo("id",dept.getId());
            }
            if(StringUtils.isNotEmpty(dept.getPId())){
                criteria.andEqualTo("pId",dept.getPId());
            }
            if(StringUtils.isNotEmpty(dept.getName())){
                criteria.andLike("name",dept.getName());
            }
        }
        criteria.andEqualTo("status", StatusEnum.OK.getCode());
        example.orderBy("createTime").desc();
        return deptMapper.selectByExample(example);
    }

    /**
     * 判断部门名称是否重复
     * @param dept
     * @return
     */
    @Override
    public Boolean isExistDeptName(Department dept){
        String pk = "";
        if(StringUtils.isEmpty(dept.getId()))
            pk = String.valueOf(Long.MIN_VALUE);
        else
            pk = dept.getId();
        Example example = new Example(Department.class);
        example.createCriteria().andNotEqualTo("id",pk)
        .andEqualTo("name",dept.getName());
        List<Department> list = deptMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(list)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteByPk(Byte status,String id){
        Department dept = new Department();
        dept.setStatus(status);
        dept.setId(id);
        deptMapper.updateByPrimaryKeySelective(dept);
    }

    @Override
    public void save(Department dept){
        if(StatusConstants.PID.equals(dept.getPId())){
            dept.setIcon(IconConstants.DEPT_PID_ICON);
        } else {
            dept.setIcon(IconConstants.DEPT_CHILD_ICON);
        }
        if(StringUtils.isEmpty(dept.getId())){
            dept.setId(CommonUtils.getUUID());
            dept.setCreateTime(new Date());
            deptMapper.insertSelective(dept);
        } else {
            dept.setUpdateTime(new Date());
            deptMapper.updateByPrimaryKeySelective(dept);
        }

    }
}
