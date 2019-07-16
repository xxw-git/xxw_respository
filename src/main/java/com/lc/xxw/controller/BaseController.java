package com.lc.xxw.controller;


import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.io.IOException;
import java.util.Map;

/**
 * @description: 所有controller的基类
 * @author: xuexiaowei
 * @create: 2019-07-12 17:20
 */

public class BaseController {

    public static final Logger logger = LogManager.getLogger(BaseController.class);

    /** 存储返回状态和数据 */
    private static JSONObject json = new JSONObject();

    protected void writeJSON(HttpServletResponse response, Integer status, Object object) {
        try {
            json.put("status", status);
            json.put("data", object);
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(json.toJSONString());
        }catch (IOException e){
            logger.error("服务器异常",e);
        }
    }

    protected void writeJSON(HttpServletResponse response, String success) {
        try {
            json.put("success", success);
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(json.toJSONString());
            response.flushBuffer();
            response.getWriter().flush();
        }catch (IOException e){
            logger.error("服务器异常",e);
        }
    }

    protected void writeJSON(HttpServletResponse response, Object object) {
        try {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(json.toJSONString(object));
        }catch (IOException e){
            logger.error("服务器异常",e);
        }

    }

    /**
     * 获取request
     * @return
     */
    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取response
     * @return
     */
    public HttpServletResponse getResponse() {
        return  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 获取session
     * @return
     */
    public HttpSession getSession(){
        return getRequest().getSession();
    }
}
