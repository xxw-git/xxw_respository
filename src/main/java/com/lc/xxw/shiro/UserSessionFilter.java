package com.lc.xxw.shiro;

import com.lc.xxw.entity.User;
import org.apache.log4j.Logger;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @description:
 * @author: xuexiaowei
 * @create: 2019-07-23 10:21
 */

public class UserSessionFilter extends AccessControlFilter {

    protected static final Logger logger = Logger.getLogger(UserSessionFilter.class);

    private static final String USER ="user";

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request,response);
        if(null == subject){
            redirectToLogin(request, response);
            return false;
        }
        HttpSession session = WebUtils.toHttp(request).getSession();
        Object object = session.getAttribute(USER);
        if(object == null){
            Object obj = subject.getPrincipal();
            if(null!= obj){
                session.setAttribute(USER, (User)obj);
            }
        }
        return this.isAccessAllowed(request,response,mappedValue) || this.onAccessDenied(request,response,mappedValue);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        if (isLoginRequest(servletRequest, servletResponse)) {
            return true;
        } else {
            Subject subject = getSubject(servletRequest, servletResponse);
            return subject.getPrincipal() != null;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return true;
    }
}
