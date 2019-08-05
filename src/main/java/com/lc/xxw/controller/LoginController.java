package com.lc.xxw.controller;

import com.google.common.collect.Lists;
import com.lc.xxw.common.enmus.ResultEnum;
import com.lc.xxw.common.enmus.StatusEnum;
import com.lc.xxw.common.utils.*;
import com.lc.xxw.common.vo.ResultVo;
import com.lc.xxw.constants.StaticPathConstants;
import com.lc.xxw.constants.StatusConstants;
import com.lc.xxw.entity.Menu;
import com.lc.xxw.entity.User;
import com.lc.xxw.service.MenuService;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.concurrent.TimeUnit;



@Controller
@RequestMapping("/")
@Api(value = "用户登录", description="用户登录")
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private MenuService menuService;



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
            newPwd = RSAUtils.decrypt(password,RSAUtils.PRIVATE_KEY);
            //newPwd = RSAUtils.decryptByPrivateKey(password,privateKey);
            logger.info("明文是：" + newPwd);
        }catch (Exception e) {
            logger.info("解密失败");
            return ResultUtil.error("用户密码输入错误");
        }

        // 2.封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(username,ShiroUtils.getStrByMD5(newPwd,username));
        // 3.执行登录，进入自定义Realm类中
        try{
            token.setRememberMe(rememberMe);
            subject.login(token);
            logger.info(subject);
            redisUtils.delete(username);
            getSession().setAttribute("basePath",getRequest().getContextPath());
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
                return ResultUtil.error(ResultEnum.USER_LOGIN_ERROR.getMsg());
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

        String loginCount = ReadProperties.getProperValueBykey(StaticPathConstants.SYSTEM_INFO_PATH,"login.count");

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
     * 登录页
     */
    @RequestMapping("/login")
    public ModelAndView home(){
        return new ModelAndView("login");
    }

    /**
     * 首页菜单
     * @return
     */
    @RequestMapping(value = "/index")
    public String index(Model model){
        try{
            List<Menu> allMenu = menuService.findAllMenu();
            //一级菜单
            List<Menu> rootMenu = Lists.newArrayList();
            for (Menu menu : allMenu) {
                if(StatusEnum.OK.getCode() == menu.getType()){
                    rootMenu.add(menu);
                }
            }
            for (Menu root : rootMenu) {
                root.setSubMenu(getMenuChild(root.getId(),allMenu));
            }
            model.addAttribute("menuList", rootMenu);
        }catch (Exception e){
            logger.debug("菜单加载失败");
        }
        return "index";
    }

    public List<Menu> getMenuChild(String id,List<Menu> menuList){
        List<Menu> childList = Lists.newArrayList();
        for (Menu nav : menuList) {
            // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
            //相等说明：为该根节点的子节点。
            if(nav.getParentId().equals(id)){
                childList.add(nav);
            }
        }
        for (Menu nav : childList) {
            nav.setSubMenu(getMenuChild(nav.getId(), menuList));
        }
        Collections.sort(childList,Menu.order());//排序
        if(childList.size() == 0){
            return new ArrayList<Menu>();
        }
        return childList;

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

    /**
     * 首页
     * @return
     */
    @RequestMapping(value = "/welcome")
    public String welcome(Model model){
        model.addAttribute("nowTime", DatetimeUtils.date2string(new Date(),DatetimeUtils.YYYY_MM_DD_HH_MM_SS));
        model.addAttribute("user",getUserInfo());
        return "welcome";
    }

}
