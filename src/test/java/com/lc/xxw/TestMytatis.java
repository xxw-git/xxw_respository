package com.lc.xxw;

import com.lc.xxw.entity.User;
import com.lc.xxw.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/applicationContext.xml",
        "classpath*:/spring-redis.xml"})

public class TestMytatis {
    private static Logger log = LogManager.getLogger(TestMytatis.class);

    @Autowired
    private UserService userService;

    @Test
    public void  test(){
       List<User> list =  userService.selectAll();
       log.info("查询结果为：" + list.size());

    }

}
