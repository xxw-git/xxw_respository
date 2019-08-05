package com.lc.xxw;

import com.lc.xxw.common.utils.ReadProperties;
import com.lc.xxw.constants.IconConstants;
import com.lc.xxw.constants.StaticPathConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Properties;

/**
 * @description: 测试读取配置文件信息
 * @author: xuexiaowei
 * @create: 2019-07-16 17:25
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:/applicationContext.xml",
        "classpath:spring-redis.xml"})
public class TestReadProperties {
    private static final Logger log = LogManager.getLogger(TestJedisClient.class);

    @Test
    public void testReadProperties(){
        log.info("ICON_PATH为：" + IconConstants.ICON_PATH);
       // log.info("登录次数为：" + IconConstants.BASE_PATH + IconConstants.ICON_PATH + value);
    }
}
