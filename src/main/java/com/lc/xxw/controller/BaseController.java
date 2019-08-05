package com.lc.xxw.controller;


import com.lc.xxw.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.session.Session;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @description: 所有controller的基类
 * @author: xuexiaowei
 * @create: 2019-07-12 17:20
 */

public class BaseController {

    public static final Logger logger = LogManager.getLogger(BaseController.class);

    /**
     * 获取request
     * @return
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取response
     * @return
     */
    public static HttpServletResponse getResponse() {
        return  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 获取session
     * @return
     */
    public static HttpSession getSession(){
        return getRequest().getSession();
    }

    /**
     * 从session中获取用户信息
     * @return
     */
    public User getUserInfo(){
        HttpSession session = getSession();
        if(session!=null && session.getAttribute("user")!=null){
            return (User)session.getAttribute("user");
        }
        return null;
    }
}
