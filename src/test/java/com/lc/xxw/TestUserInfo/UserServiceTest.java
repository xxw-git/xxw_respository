package com.lc.xxw.TestUserInfo;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.lc.xxw.constants.StatusConstants;
import com.lc.xxw.entity.User;
import com.lc.xxw.service.UserService;
import com.lc.xxw.shiro.ShiroUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @description: 用户管理接口测试
 * @author: xuexiaowei
 * @create: 2019-07-13 10:25
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml","classpath:spring-redis.xml"})
public class UserServiceTest {

    private static final Logger log = LogManager.getLogger(UserServiceTest.class);

    @Autowired
    private UserService userService;

    @Test
    public void testUserSave(){
        User user = new User();
        user.setId("A691331E688A44338CC1E06F7022E5E1");
        user.setLoginAccount("admin");
        user.setPassword(ShiroUtils.getStrByMD5("123",user.getLoginAccount()));
        user.setStatus(StatusConstants.OK);
        user.setUserName("测试用户名");
        user.setLxfs("17600145358");
        user.setEmail("846572293@qq.com");
        user.setSfzh("123456");
        JSONObject obj = userService.save(user);
        log.info(obj.getString("message"));
    }

    @Test
    public void testUserPage(){
        PageInfo<User> pageInfo = userService.findByPage(1,10);
        log.info(pageInfo.getList().size());
        log.info("共" + pageInfo.getTotal() +"条数据。");
    }

}
