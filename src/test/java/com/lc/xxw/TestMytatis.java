package com.lc.xxw;

import com.google.common.collect.Maps;
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
import org.springframework.test.context.web.WebAppConfiguration;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:/applicationContext.xml",
        "classpath*:/spring-redis.xml"})

public class TestMytatis {
    private static Logger log = LogManager.getLogger(TestMytatis.class);

    @Autowired
    private UserService userService;

    @Test
    public void  test(){
        Map<String,Object> map = Maps.newHashMap();
       List<User> list =  userService.selectAll(map);
       log.info("查询结果为：" + list.get(0).getLoginAccount());

    }

    @Test
    public void testRSA() throws Exception {
        /*String data = "123";
        String modulusString = "95701876885335270857822974167577168764621211406341574477817778908798408856077334510496515211568839843884498881589280440763139683446418982307428928523091367233376499779842840789220784202847513854967218444344438545354682865713417516385450114501727182277555013890267914809715178404671863643421619292274848317157";
        String publicExponentString = "65537";
        String privateExponentString = "15118200884902819158506511612629910252530988627643229329521452996670429328272100404155979400725883072214721713247384231857130859555987849975263007110480563992945828011871526769689381461965107692102011772019212674436519765580328720044447875477151172925640047963361834004267745612848169871802590337012858580097";
        RSAPublicKey publicKey = RSAUtils.getPublicKey(modulusString,publicExponentString);
        log.info("公钥转成String是：" + RSAUtils.getKeyString(publicKey));
        RSAPrivateKey privateKey = RSAUtils.getPrivateKey(modulusString,privateExponentString);
        log.info("私钥转成String是：" + RSAUtils.getKeyString(privateKey));
        RSAPublicKey publicKey1 = RSAUtils.getPublicKey(RSAUtils.getKeyString(publicKey));
        String encrypted=RSAUtils.encryptByPublicKey(data, publicKey1);
        //String encrypted = "d2bhnyXuslIvTWl5B+64GrE8xBsxL/RojaKnId4EaD/Ui9OX5utwjMw7AQNKo1qpBKYi91en6wu6BxUPRy93xzGOfHTbQfIH/z6W330oUZPchjCeGF45m4W5vKKxf1Oh2761ysGPtbenMM67ls3c/tDoAnw1DNlUEqf2n5xwl7o=";
        System.out.println("加密后："+encrypted);
        RSAPrivateKey privateKey1 = RSAUtils.getPrivateKey(RSAUtils.getKeyString(privateKey));
        //私钥解密
        String decrypted=RSAUtils.decryptByPrivateKey(encrypted,  privateKey1);
        System.out.println("解密后："+ decrypted);*/

        HashMap<String, Object> map = RSAUtils.getKeys();
        log.info("公钥是：" + RSAUtils.getKeyString((RSAPublicKey)map.get("public")));
        log.info("私钥是：" + RSAUtils.getKeyString((RSAPrivateKey)map.get("private")));
        RSAPrivateKey privateKey = (RSAPrivateKey)map.get("private");
        String test = "123";
        //String tt = RSAUtils.encryptByPublicKey(test,(RSAPublicKey)map.get("public"));
        String tt = RSAUtils.encrypt(test,RSAUtils.getKeyString((RSAPublicKey)map.get("public")));
       // log.info("公钥加密后：" + tt);
        //String t22 = RSAUtils.decryptByPrivateKey(tt,(RSAPrivateKey)map.get("private"));
        //log.info("解密后：" + t22);

        String ss = RSAUtils.decrypt(tt,RSAUtils.getKeyString((RSAPrivateKey)map.get("private")));
       log.info("解密后：" + ss);

    }

}
