package com.lc.xxw.controller;

import com.lc.xxw.common.utils.RSAUtils;
import com.lc.xxw.common.utils.ReadProperties;
import com.lc.xxw.common.utils.RedisUtils;
import com.lc.xxw.common.utils.ResultUtil;
import com.lc.xxw.common.vo.ResultVo;
import com.lc.xxw.constants.StatusConstants;
import com.lc.xxw.entity.User;
import com.lc.xxw.service.UserService;
import com.lc.xxw.shiro.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Properties;
import java.util.concurrent.TimeUnit;



@Controller
@RequestMapping("/")
@Api(value = "用户登录", description="用户登录")
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtils redisUtils;

    private static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJvJzknlroeIOBY1C9ILl4n+z9PW" +
            "dM3DCRbhDNPLAFkp2fX+43MBO7sfFOin8b5UySWfmyilrSfdsEw3B3ZCR4+JvX0D0+BCfwUezmtw" +
            "z4LeM5S6c3JUxG6pyxVXaZwN9HH2XrxP+r9e2DymNDmwNh53RxD0LSl9/Na3HdIBPzNzAgMBAAEC" +
            "gYEAmtDp2DYQQ0/zrN36aTpr1g8LqZEtcm2n0rzDapYKOpGEsRokHl3TZhl1Rd/gNS08187M+o/q" +
            "i/ua/6KQH82uHj8aZrOQeaJT0sk6o6p7urij2y+7TyUMtmzFBf2O3dHBGQvTjutxturxrp0ii4F+" +
            "tnVUfFKBIc3CtJLuvmtNRAECQQDhCGkrhpn/a8YBELpmD7JJ9xl6/Q5LjvuiqfBXzrmYfVCVqvYO" +
            "u54tgl9m05Axczu9TIcjEjHW6NbEudIGb7GzAkEAsToB5buAvXjb8qOShPs6D/sfQULC3//qO6Fe" +
            "mNI3RAFi7D2PGlh/QpjGVAhsORGYNy8j/9gAnnnH008lZGQXQQJAKTfFK7fH1UUES4Wo3rDZUzrz" +
            "a9eWGrjh1nWSFENFM20gqYla8G/lFSjgGJF/w877jjzKM95NSrPzQq1Wjt8+iQJAFnxQn1Ax3lhG" +
            "N7vPLDYfwMVQytvok7kJg/VOZj9NqcAvR9/rlyEhTFbL2v+Sk48K6/18KMrEEVdMJiBFkz4rwQJB" +
            "AL1/gyo+cYKGJo2Og3fohHzmycxbITjahyK98AeD3oS+ZjO6HReb2ZAnHTQMcxarhUujUu/lAGYf" +
            "5AHAxHa7apo=";

    @ApiOperation(notes = "用户登录验证",value = "用户登录验证")
    @RequestMapping(method = RequestMethod.POST,value = "/submitLogin")
    @ResponseBody
    public ResultVo login(@ApiParam(value = "用户账号",name = "username",required = true) @RequestParam("username")String username,
                          @ApiParam(value = "用户密码",name = "password",required = true)@RequestParam("password")String password,
                          @ApiParam(value = "记住我",name = "rememberMe")@RequestParam("rememberMe")boolean rememberMe){
        // 1.获取Subject主体对象
        Subject subject = SecurityUtils.getSubject();
        String newPwd = null;
        logger.info("客户端密码：" + password);
        try{
            password = password.replace("%2B","+");
            logger.info("客户端处理后的密码：" + password);
            //RSAPrivateKey privateKey = RSAUtils.getPrivateKey(PRIVATE_KEY);
            newPwd = RSAUtils.decrypt(password,PRIVATE_KEY);
            //newPwd = RSAUtils.decryptByPrivateKey(password,privateKey);
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
            return ResultUtil.success("登录成功");
        } catch (LockedAccountException dax) {
            logger.info("账号为:" + username + " 用户已经被禁用！");
            return ResultUtil.error("帐号已被禁用！");
        } catch (ExcessiveAttemptsException eae) {
            logger.info("账号为:" + username + " 用户登录次数过多，有暴力破解的嫌疑！");
            return ResultUtil.error("登录次数过多！");
        }catch (AccountException ae) {
            logger.info("账号为:" + token.getPrincipal() + " 帐号或密码错误！");
            String excessiveInfo = ExcessiveAttemptsInfo(username);
            if(null!=excessiveInfo){
                return ResultUtil.error(excessiveInfo);
            }else{
                return ResultUtil.error("帐号或密码错误！");
            }
        } catch (AuthenticationException ae) {
            logger.error(ae);
            logger.info("------------------身份认证失败-------------------");
            return ResultUtil.error("身份认证失败！");
        } catch (Exception e) {
            logger.error(e);
            logger.info("未知异常信息。。。。");
            return ResultUtil.error("登录认证错误！");
        }
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
    @RequestMapping(value = "/index")
    public String index(){
        return "index";
    }

    /**
     * 权限不足
     * @return
     */
    @RequestMapping(value = "/unauthorized")
    public String unauthorized(){
        return "unauthorized";
    }

    /**
     * 错误页面
     * @return
     */
    @RequestMapping(value = "/error")
    public String error(){
        return "error";
    }

}
