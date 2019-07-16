package com.lc.xxw.controller;

import com.alibaba.fastjson.JSONObject;
import com.lc.xxw.common.utils.RSAUtils;
import com.lc.xxw.common.utils.ReadProperties;
import com.lc.xxw.common.utils.RedisUtils;
import com.lc.xxw.constants.StatusConstants;
import com.lc.xxw.entity.User;
import com.lc.xxw.service.UserService;
import com.lc.xxw.shiro.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Properties;
import java.util.concurrent.TimeUnit;


@Controller
@RequestMapping("/")
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtils redisUtils;

    private static final String PRIVATE_KEY = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEArGjlrj9T4E6gqA9qxHdLVEzp83BD" +
            "CuHVdNLjmIsxgfyHDtn5tateHXQhGgGN0rJSTV6nWSE76KSvdH0A/PgQnwIDAQABAkBkXrj/xPxG" +
            "hF/BFyCX+b8P96rnPv64shp7ZV58auRKgEp4PVEu44lu/7hCJxcXS7lQTwitYED7/hzKk9E/Zxv5" +
            "AiEA9QmSQbnmiYg5wLop77DSqjhrYCApxvISfTGgPKTZBQ0CIQC0H4RaQKWpPZVQIXjaauJv4wbg" +
            "sZHLPIimUrhmoGkZWwIgCTSI2AtBy9zgPos/1A9Seq6P6haLOzwQ0b8xg9W1iWkCIEZpZ6CsUtYU" +
            "x9CaNRcU302jruWZJIgRMs3p2kHsBQmvAiEA7OaHNyqMruM/jCKhnc8bD9bA7+N1tb14IElJqUavLGY=";

    @RequestMapping(method = RequestMethod.POST,value = "/submitLogin.do")
    @ResponseBody
    public JSONObject login(@RequestParam("username")String username, @RequestParam("password")String password,
            @RequestParam("rememberMe")boolean rememberMe){
        JSONObject object = new JSONObject();
        // 1.获取Subject主体对象
        Subject subject = SecurityUtils.getSubject();
        String newPwd = null;
        logger.info("客户端密码：" + password);
        try{
            newPwd = password.replace("%2B","+");
            logger.info("客户端处理后的密码：" + newPwd);
            newPwd = RSAUtils.decrypt(newPwd,PRIVATE_KEY);
            logger.info("明文是：" + newPwd);
        }catch (Exception e) {
            logger.info("解密失败");
        }

        // 2.封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(username,ShiroUtils.getStrByMD5(newPwd,username));
        // 3.执行登录，进入自定义Realm类中
        try{
            token.setRememberMe(rememberMe);
            subject.login(token);
            logger.info(subject);
            redisUtils.delete(username);
            logger.info("------------------身份认证成功-------------------");
            object.put("status", 200);
            object.put("message", "登录成功！");
        } catch (LockedAccountException dax) {
            logger.info("账号为:" + username + " 用户已经被禁用！");
            object.put("status", 500);
            object.put("message", "帐号已被禁用！");
        } catch (ExcessiveAttemptsException eae) {
            logger.info("账号为:" + username + " 用户登录次数过多，有暴力破解的嫌疑！");
            object.put("status", 500);
            object.put("message", "登录次数过多！");
        }catch (AccountException ae) {
            logger.info("账号为:" + token.getPrincipal() + " 帐号或密码错误！");
            String excessiveInfo = ExcessiveAttemptsInfo(username);
            if(null!=excessiveInfo){
                object.put("status", 500);
                object.put("message", excessiveInfo);
            }else{
                object.put("status", 500);
                object.put("message", "帐号或密码错误！");
            }
        } catch (AuthenticationException ae) {
            logger.error(ae);
            logger.info("------------------身份认证失败-------------------");
            object.put("status", 500);
            object.put("message", "身份认证失败！");
        } catch (Exception e) {
            logger.error(e);
            logger.info("未知异常信息。。。。");
            object.put("status", 500);
            object.put("message", "登录认证错误！");
        }
        return object;
    }

    /**
     * 验证器，增加了登录次数校验功能
     * @param account 登录账号
     * @return
     */
    public String ExcessiveAttemptsInfo(String account){
        String excessiveInfo = null;
        StringBuffer userName = new StringBuffer(account);
        userName.append("ExcessiveCount");
        String accountKey = userName.toString();

        Properties pro = ReadProperties.getProperties();
        String loginCount = pro.getProperty("login.count");

        if(null == redisUtils.get(accountKey)){
            //过期时间设置30分钟
            redisUtils.setForTimeCustom(accountKey, "1",30, TimeUnit.MINUTES);
        }else{
            int count = Integer.parseInt(redisUtils.get(accountKey))+1;
            int time = 30-(2*count);
            redisUtils.setForTimeCustom(accountKey,String.valueOf(count),time,TimeUnit.MINUTES);
        }
        /**登录错误3次,发出警告*/
        if(Integer.parseInt(redisUtils.get(accountKey))==3){
            excessiveInfo = "账号密码错误3次,再错2次账号将被禁用！";
        }
        /**登录错误5次,该账号将被禁用*/
        if(Integer.parseInt(redisUtils.get(accountKey))==Integer.parseInt(loginCount)){
            User user = userService.findUserByUsername(account);
            if(null!=user){
                userService.updateDisabled(user.getId(), StatusConstants.FREEZED);
            }
            redisUtils.delete(account);
            excessiveInfo = "账号密码错误过多,账号已被禁用！";
        }
        return excessiveInfo;
    }

    /**
     * 首页访问
     */
    @RequestMapping("/login")
    public ModelAndView home(){
        return new ModelAndView("login");
    }

    /**
     * 首页
     * @return
     */
    @RequestMapping(value = "/index.do")
    public String index(){
        return "index";
    }

    /**
     * 权限不足
     * @return
     */
    @RequestMapping(value = "/unauthorized.do")
    public String unauthorized(){
        return "unauthorized";
    }

    /**
     * 错误页面
     * @return
     */
    @RequestMapping(value = "/error.do")
    public String error(){
        return "error";
    }

}
