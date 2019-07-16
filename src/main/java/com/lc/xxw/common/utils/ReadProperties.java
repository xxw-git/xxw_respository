package com.lc.xxw.common.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @description: 读取配置文件信息
 * @author: xuexiaowei
 * @create: 2019-07-16 17:18
 */

public class ReadProperties {

    private static final Logger logger = LogManager.getLogger(ReadProperties.class);

    public static Properties getProperties(){
        Properties properties = new Properties();
        try{
            InputStream input = ReadProperties.class.getResourceAsStream("/systemInfo.properties");
            properties.load(input);
            return properties;
        }catch (IOException e){
            logger.error("配置文件读取失败。",e);
        }
        return null;
    }
}
