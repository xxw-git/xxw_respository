package com.lc.xxw.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.lc.xxw.common.enmus.StatusEnum;
import com.lc.xxw.common.utils.ResultUtil;
import com.lc.xxw.common.utils.StringUtils;
import com.lc.xxw.common.vo.ResultVo;
import com.lc.xxw.entity.PageValid;
import com.lc.xxw.entity.Role;
import com.lc.xxw.entity.User;
import com.lc.xxw.service.RoleService;
import com.lc.xxw.service.UserService;
import com.lc.xxw.shiro.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @description: 用户管理
 * @author: xuexiaowei
 * @create: 2019-07-13 08:42
 */

@Controller
@RequestMapping("/sys/user")
@Api(value = "用户管理",description = "用户管理")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    private static final String USER_PATH = "/user/";


    /**
     * 列表页面
     * @return
     */
    @RequestMapping(value = "index")
    @RequiresPermissions("system:user:index")
    public String index(){
        return USER_PATH + "list";
    }

    @RequestMapping(value = "list",method = RequestMethod.POST)
    @ApiOperation(value = "加载用户列表",notes = "加载用户列表")
    @ResponseBody
    public ResultVo list(@RequestBody Map<String,Object> param){
        try{
            PageValid page = new PageValid();
            if(param.containsKey("currentPage") && param.get("currentPage")!=null){
                page.setCurrentPage(Integer.valueOf(param.get("currentPage").toString()));
            }
            if(param.containsKey("limit") && param.get("limit")!=null){
                page.setPageSize(Integer.valueOf(param.get("limit").toString()));
            }
            PageInfo<User> pageInfo = userService.findByPage(page);
            return ResultUtil.success(Integer.valueOf(pageInfo.getTotal()+""),pageInfo.getList());
        }catch (Exception e){
            return ResultUtil.error("");
        }

    }

    /**
     * 跳转修改密码页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/updatePassword/{id}")
    public String updatePassword(@PathVariable String id,Model model){
        model.addAttribute("id",id);
        return USER_PATH + "updatePassword";
    }

    /**
     * 校验原密码是否输入正确
     * @return
     */
    @PostMapping("/checkOldPwd")
    @ResponseBody
    public ResultVo checkOldPwd(@RequestBody JSONObject param){
        String id = param.getString("id");
        String oldPwd = param.getString("password");
        if(StringUtils.isEmpty(oldPwd)){
            return ResultUtil.error("请输入原密码");
        }
        User user = userService.selectUserByPk(id);
        if(user.getPassword().equals(ShiroUtils.getStrByMD5(oldPwd,user.getLoginAccount()))){
            return ResultUtil.success(true);
        } else {
            return ResultUtil.error("原密码输入错误");
        }
    }

    /**
     * 修改密码
     * @return
     */
    @PostMapping("/saveNewPwd")
    @ResponseBody
    public ResultVo saveNewPwd(@RequestBody JSONObject param){
        String id = param.getString("id");
        String newPwd = param.getString("newPwd");
        if(StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(newPwd)){
            User user = userService.selectUserByPk(id);
            user.setPassword(ShiroUtils.getStrByMD5(newPwd,user.getLoginAccount()));
            int result = userService.update(user);
            if(result>0)
                return ResultUtil.success("修改成功");
            else
                return ResultUtil.error("修改失败");

        } else {
            return ResultUtil.error("修改失败");
        }
    }

    /**
     * 跳转新增页面
     * @return
     */
    @RequestMapping("/add")
    @RequiresPermissions("system:user:add")
    public String add(){
        return USER_PATH + "add";
    }

    /**
     * 跳转编辑页面
     * @return
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("system:user:edit")
    public String edit(@PathVariable String id, Model model){
        User user = userService.selectUserByPk(id);
        List<String> roleIds = roleService.selectRolesByUserId(id);
        model.addAttribute("userEntity",user);
        model.addAttribute("roleIds",roleIds);
        return USER_PATH + "edit";
    }


    /**
     * 保存添加/修改的用户数据
     * @param user
     * @return
     */
    @RequestMapping(value = "save",method = RequestMethod.POST)
    @ApiOperation(value = "保存用户信息",notes = "保存用户信息")
    @RequiresPermissions({"system:user:add", "system:user:edit"})
    @ResponseBody
    public ResultVo save(@RequestBody User user){
        try {

            if(StringUtils.isEmpty(user.getId())){
                user.setPassword(ShiroUtils.getStrByMD5(user.getPassword(),user.getLoginAccount()));
            }
            Boolean isRepeat = userService.repeatByUserName(user.getId(),user.getLoginAccount());
            //判断账号是否存在
            if(isRepeat){
                return ResultUtil.error("用户账号已存在。");
            }
            userService.save(user);
            //新增到用户角色中间表
            if(StringUtils.isNotEmpty(user.getRoleIds())){
                String[] roleId = user.getRoleIds().split(",");
                for (String r_id: roleId) {
                    roleService.insertUserRole(r_id,user.getId());
                }
            }
            return ResultUtil.SAVE_SUCCESS;
        }catch (Exception e){
            logger.error("操作失败",e);
            return ResultUtil.error("操作失败");
        }

    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @GetMapping("/delete/{ids}")
    @RequiresPermissions("system:user:del")
    @ResponseBody
    public ResultVo delete(@PathVariable String ids){
        if(StringUtils.isEmpty(ids)){
            return ResultUtil.error("删除失败");
        }
        String[] id = StringUtils.split(ids,",");
        userService.updateStatus(id, StatusEnum.DELETE.getCode());
        return ResultUtil.success("删除成功");
    }

    @RequestMapping("/role")
    @ResponseBody
    public ResultVo roleList(){
        List<Role> roleList = roleService.selectAll();
        return ResultUtil.success(roleList.size(),roleList);
    }

}
