package com.lc.xxw.shiro;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.apache.shiro.crypto.hash.Md5Hash;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.Map;

/**
 * @description: Shiro工具类
 * @author: xuexiaowei
 * @create: 2019-07-12 09:21
 */

public class ShiroUtils {
    protected final static Logger logger = Logger.getLogger(ShiroUtils.class);

    /**
     * @description: 加盐（任意字符）
     * @param password 密码
     * @param salt 盐
     * @return
     */
    public static String getStrByMD5(String password,String salt){
        return new Md5Hash(password,salt,10).toString();
    }

    /**
     * @description:密码加密
     * @param password
     * @return
     * @throws Exception
     */
    public static String getMD5Str(String password) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(password);
        String str = sb.toString();
        MessageDigest md = MessageDigest.getInstance("MD5");
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for(int i=0;i<charArray.length;i++){
            byteArray[i] = (byte) charArray[i];
        }
        byte[] mdBytes = md.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for(int i=0;i<mdBytes.length;i++){
            int val = ((int)mdBytes[i])&0xff;
            if(val<16){
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * @description:是否为 Ajax 请求
     * @param request
     * @return
     */
    public static boolean isAjax(ServletRequest request){
        return "XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"));
    }

    /**
     *@description: 输出JSON
     * @param response
     * @param resultMap
     */
    public static void out(ServletResponse response, Map<String, String> resultMap){

        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(JSONObject.toJSON(resultMap).toString());
        } catch (Exception e) {
            logger.debug("输出JSON报错!",e);
            logger.info("输出JSON报错!");
        }finally{
            if(null != out){
                out.flush();
                out.close();
            }
        }
    }
}
