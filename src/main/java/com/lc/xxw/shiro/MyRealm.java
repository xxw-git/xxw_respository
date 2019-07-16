package com.lc.xxw.shiro;

import com.lc.xxw.common.enmus.StatusEnum;
import com.lc.xxw.entity.User;
import com.lc.xxw.service.UserService;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.*;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Set;

public class MyRealm extends AuthorizingRealm {

    private static final Logger logger = Logger.getLogger(MyRealm.class);

    @Autowired
    private UserService userService;

    /**
     * 授权 在配有缓存的情况下，只加载一次
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //当前登录用户，账号
        String account = principalCollection.getPrimaryPrincipal().toString() ;
        logger.info("当前登录账号：" + account);
        User user = userService.findUserByUsername(account);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo() ;
        //获取角色信息
        Set<String> roleName = userService.findRolesByUserId(user.getId()) ;
        //获取权限信息
        Set<String> permissions = userService.findPermissions(user.getId()) ;
        info.setRoles(roleName);
        info.setStringPermissions(permissions);
        return info;
    }

    /**
     * 认证登录
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.将token转换为UsernamePasswordToken
        UsernamePasswordToken userToken = (UsernamePasswordToken)token;
        //获取用户账号
        String account = userToken.getUsername();
        //获取密码
        String password = new String((char[])token.getCredentials());
        User user = userService.login(account,password);
        if(user == null){
            throw new AccountException("账号不存在");
        }
        if(user.getStatus().equals(StatusEnum.FREEZED.getCode())){
            throw new LockedAccountException("账号" + user.getLoginAccount()+"被锁定");
        }
        if (user != null){
            //将查询到的用户账号和密码存放到 authenticationInfo用于后面的权限判断。
            ByteSource salt = ByteSource.Util.bytes(user.getPasswordSalt());
            SimpleAuthenticationInfo simpleAuthorizationInfo = new SimpleAuthenticationInfo(user,user.getPassword(),
                    salt,getName());
            return simpleAuthorizationInfo ;
        }else{
            return  null ;
        }
    }

}
