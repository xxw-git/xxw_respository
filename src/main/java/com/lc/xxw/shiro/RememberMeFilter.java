package com.lc.xxw.shiro;

import com.lc.xxw.entity.User;
import com.lc.xxw.mapper.UserMapper;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * @description: remeberMe拦截器
 * @author: xuexiaowei
 * @create: 2019-07-12 08:41
 */

public class RememberMeFilter extends FormAuthenticationFilter {

    protected static final Logger logger = Logger.getLogger(RememberMeFilter.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue){
        Subject subject = getSubject(request,response);
        Session session = subject.getSession();
        /**满足三个条件：选择记住我,非密码登录,session为空*/
        if(!subject.isAuthenticated() && subject.isRemembered() && session.getAttribute("currentUser") == null){
            Object principal = subject.getPrincipal();
            if(principal!=null){
                User u = new User();
                u.setLoginAccount(principal.toString());
                User user = userMapper.selectOne(u);
                session.setAttribute("currentUser", user);
            }
        }
        return subject.isAuthenticated()||subject.isRemembered();
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token,Subject subject,
                                     ServletRequest request,ServletResponse response) throws Exception{
        //获取已登录的用户信息
        User user = (User) subject.getPrincipal();
        HttpServletRequest req = WebUtils.toHttp(request);
        HttpSession session = req.getSession();
        session.setAttribute("user",user);
        return super.onLoginSuccess(token,subject,request,response);
    }


}
