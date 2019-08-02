package com.lc.xxw.controller;

import com.google.common.collect.Lists;
import com.lc.xxw.common.utils.ResultUtil;
import com.lc.xxw.common.vo.ResultVo;
import com.lc.xxw.constants.StatusConstants;
import com.lc.xxw.entity.Department;
import com.lc.xxw.service.DeptService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 部门管理
 * @className: DeptController
 * @author: xuexiaowei
 * @create: 2019-08-02 10:15
 */
@Controller
@RequestMapping("/sys/dept")
@Api(value = "部门管理")
public class DeptController extends BaseController {

    @Autowired
    private DeptService deptService;

    /**
     * 获取tree
     * @return
     */
    @GetMapping(value = "/tree")
    @ResponseBody
    public ResultVo getTree(){
        List<Department> rootList = Lists.newArrayList();
        try {
            List<Department> allList = deptService.selectByDept(new Department());
            for (Department root : allList) {
                if(StatusConstants.PID.equals(root.getParentId())){
                    rootList.add(root);
                }
            }
            for (Department root : rootList) {
                root.setChildren(getChildren(root.getId(),allList));
            }
            return ResultUtil.success(rootList);

        } catch (Exception e){
            logger.error("获取部门树形列表失败",e);
            return ResultUtil.error(rootList);
        }

    }

    private List<Department> getChildren(String id, List<Department> allList) {
        List<Department> children = Lists.newArrayList();
        for (Department dept: allList) {
            if(dept.getParentId().equals(id)){
                children.add(dept);
            }
        }
        for (Department dept : children) {
            dept.setChildren(getChildren(dept.getId(), allList));
        }
        if(children.size() == 0){
            return new ArrayList<Department>();
        }
        return children;
    }

}
