package com.lc.xxw.shiro;

import org.apache.log4j.Logger;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @description: 自定义登出
 * @author: xuexiaowei
 * @create: 2019-07-11 15:43
 */
@Service
public class SystemLogoutFilter extends LogoutFilter {

    private static final Logger log = Logger.getLogger(SystemLogoutFilter.class);

    /**
     * @description: 重写preHandle方法
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        //在这里执行退出系统前需要清空的数据
        Subject subject = getSubject(request, response);
        String redirectUrl = getRedirectUrl(request, response, subject);
        HttpSession session = WebUtils.toHttp(request).getSession();
        if(session.getAttribute("user") !=null){
            session.removeAttribute("user");
        }
        try {
            subject.logout();
        } catch (SessionException var6) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", var6);
        }

        this.issueRedirect(request, response, redirectUrl);

        //返回false表示不执行后续的过滤器，直接返回跳转到登录页面
        return false;
    }

}
