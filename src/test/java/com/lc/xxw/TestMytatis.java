package com.lc.xxw;

import com.lc.xxw.common.utils.RSAUtils;
import com.lc.xxw.entity.User;
import com.lc.xxw.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
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

    @Test
    public void testRSA() throws Exception {
        HashMap<String, Object> map = RSAUtils.getKeys();
        log.info("公钥是：" + RSAUtils.getKeyString((RSAPublicKey)map.get("public")));
        log.info("私钥是：" + RSAUtils.getKeyString((RSAPrivateKey)map.get("private")));
        String test = "123";
        //String tt = RSAUtils.encryptByPublicKey(test,(RSAPublicKey)map.get("public"));
        String tt = RSAUtils.encrypt(test,RSAUtils.getKeyString((RSAPublicKey)map.get("public")));
        log.info("公钥加密后：" + tt);
        //String t22 = RSAUtils.decryptByPrivateKey(tt,(RSAPrivateKey)map.get("private"));
        //log.info("解密后：" + t22);

        String ss = RSAUtils.decrypt(tt,RSAUtils.getKeyString((RSAPrivateKey)map.get("private")));
        log.info("解密后：" + ss);
    }

}
