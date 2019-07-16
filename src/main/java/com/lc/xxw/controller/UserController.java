package com.lc.xxw.controller;

import com.alibaba.fastjson.JSONObject;
import com.lc.xxw.entity.User;
import com.lc.xxw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @description: 用户管理
 * @author: xuexiaowei
 * @create: 2019-07-13 08:42
 */

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/save.do",method = RequestMethod.POST)
    @ResponseBody
    public void save(@RequestBody User user){
        HttpServletResponse response = getResponse();
        JSONObject obj = new JSONObject();
        try {
            obj = userService.save(user);
            writeJSON(response,obj);
        }catch (Exception e){
            logger.error("操作失败",e);
            obj.put("status",500);
            obj.put("message","操作异常");
            writeJSON(response,obj);
        }

    }

}
