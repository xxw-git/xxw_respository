package com.lc.xxw.common.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

/**
 * @description: 读取配置文件信息
 * @author: xuexiaowei
 * @create: 2019-07-16 17:18
 */

public class ReadProperties {

    private static final Logger logger = LogManager.getLogger(ReadProperties.class);

    /**
     * 读取系统配置文件信息
     * @param key
     * @return
     */
    public static String getProperValueBykey(String path,String key){
        Properties properties = new Properties();
        String value = "";
        try{
            InputStream input = ReadProperties.class.getResourceAsStream(path);
            properties.load(input);
            Iterator<String> iterator = properties.stringPropertyNames().iterator();
            while (iterator.hasNext()){
               if(key.equals(iterator.next())){
                    value = properties.getProperty(key);
                    break;
               }
            }
            input.close();
            return value;
        }catch (IOException e){
            logger.error("配置文件读取失败。",e);
        }
        return null;
    }

}
