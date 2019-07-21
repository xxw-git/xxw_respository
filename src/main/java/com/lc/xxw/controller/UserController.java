package com.lc.xxw.controller;

import com.lc.xxw.common.utils.ResultUtil;
import com.lc.xxw.common.vo.ResultVo;
import com.lc.xxw.entity.User;
import com.lc.xxw.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * @description: 用户管理
 * @author: xuexiaowei
 * @create: 2019-07-13 08:42
 */

@Controller
@RequestMapping("/user")
@Api(value = "用户管理",description = "用户管理")
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/save.do",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "保存用户信息",notes = "保存用户信息")
    public ResultVo save(@RequestBody User user){
        try {
            userService.save(user);
            return ResultUtil.SAVE_SUCCESS;
        }catch (Exception e){
            logger.error("操作失败",e);
            return ResultUtil.error("操作失败");
        }

    }

}
