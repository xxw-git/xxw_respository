package com.lc.xxw.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lc.xxw.common.enmus.StatusEnum;
import com.lc.xxw.common.utils.ResultUtil;
import com.lc.xxw.common.utils.StringUtils;
import com.lc.xxw.common.vo.ResultVo;
import com.lc.xxw.constants.IconConstants;
import com.lc.xxw.constants.StatusConstants;
import com.lc.xxw.entity.Department;
import com.lc.xxw.entity.PageValid;
import com.lc.xxw.entity.User;
import com.lc.xxw.service.DeptService;
import com.lc.xxw.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private UserService userService;

    private static final String DEPT_PATH = "/dept/";

    /**
     * 跳转部门列表页面
     * @return
     */
    @GetMapping("/index")
    public String index(){
        return DEPT_PATH + "list";
    }

    /**
     * 保存
     * @param dept
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public ResultVo save(Department dept){
        try{
            if(StringUtils.isEmpty(dept.getName())){
                return ResultUtil.error("请输入部门名称");
            }
            if(!deptService.isExistDeptName(dept)){
                return ResultUtil.error("部门名称已存在");
            }
            deptService.save(dept);
            return ResultUtil.success("保存成功");
        }catch (Exception e){
            logger.error("部门信息保存失败");
            return ResultUtil.error("操作失败");
        }
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResultVo delete(@PathVariable String id){
        if(StringUtils.isEmpty(id)){
            return ResultUtil.error("请选择要删除的部门");
        }
        deptService.deleteByPk(StatusEnum.DELETE.getCode(),id);
        return ResultUtil.success("删除功能");
    }

    /**
     * 拖拽节点
     * @param dept
     * @return
     */
    @PostMapping("/onDrag")
    @ResponseBody
    public ResultVo onDrag(Department dept){
        try{
            if(StringUtils.isEmpty(dept.getId())){
                return ResultUtil.error("操作失败");
            }
            deptService.save(dept);
            return ResultUtil.success("操作成功");
        }catch (Exception e){
            return ResultUtil.error("操作失败。");
        }
    }


    /**
     * 获取部门成员列表
     * @param param
     * @return
     */
    @GetMapping("/getDeptMember")
    @ResponseBody
    public ResultVo getDeptMember(@RequestBody Map<String,String> param){
        try {
            Map<String,Object> filterMap = Maps.newHashMap();
            filterMap.put("deptId",param.getOrDefault("deptId",""));
            PageValid page = new PageValid();
            if(param.containsKey("currentPage") && param.get("currentPage")!=null){
                page.setCurrentPage(Integer.valueOf(param.get("currentPage")));
            }
            if(param.containsKey("limit") && param.get("limit")!=null){
                page.setPageSize(Integer.valueOf(param.get("limit")));
            }
            page.setFilterMap(filterMap);
            PageInfo<User> pageInfo = userService.findByPage(page);
            return ResultUtil.success(pageInfo.getTotal(),pageInfo.getList());
        }catch (Exception e){
            return ResultUtil.error("查询失败");
        }
    }

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
                if(StatusConstants.PID.equals(root.getPId())){
                    root.setIcon(IconConstants.BASE_PATH + IconConstants.ICON_PATH + root.getIcon());
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
            if(dept.getPId().equals(id)){
                children.add(dept);
            }
        }
        for (Department dept : children) {
            dept.setIcon(IconConstants.BASE_PATH + IconConstants.ICON_PATH + dept.getIcon());
            dept.setChildren(getChildren(dept.getId(), allList));
        }
        if(children.size() == 0){
            return new ArrayList<Department>();
        }
        return children;
    }

}
