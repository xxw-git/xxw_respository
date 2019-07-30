package com.lc.xxw.common.utils;


import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;  
import java.security.KeyPair;  
import java.security.KeyPairGenerator;  
import java.security.NoSuchAlgorithmException;  
import java.security.interfaces.RSAPrivateKey;  
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Encoder;
  
/**

* Copyright (C),2017, Guangzhou ChangLing info. Co., Ltd.

* FileName: RSAUtils.java

* RSA加密通用类

* @author hey
    * @Date    2017年11月1日 下午4:43:35

* @version 1.00

*/
@SuppressWarnings({ "restriction", "unused" })
public class RSAUtils {

    public static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJvJzknlroeIOBY1C9ILl4n+z9PW" +
            "dM3DCRbhDNPLAFkp2fX+43MBO7sfFOin8b5UySWfmyilrSfdsEw3B3ZCR4+JvX0D0+BCfwUezmtw" +
            "z4LeM5S6c3JUxG6pyxVXaZwN9HH2XrxP+r9e2DymNDmwNh53RxD0LSl9/Na3HdIBPzNzAgMBAAEC" +
            "gYEAmtDp2DYQQ0/zrN36aTpr1g8LqZEtcm2n0rzDapYKOpGEsRokHl3TZhl1Rd/gNS08187M+o/q" +
            "i/ua/6KQH82uHj8aZrOQeaJT0sk6o6p7urij2y+7TyUMtmzFBf2O3dHBGQvTjutxturxrp0ii4F+" +
            "tnVUfFKBIc3CtJLuvmtNRAECQQDhCGkrhpn/a8YBELpmD7JJ9xl6/Q5LjvuiqfBXzrmYfVCVqvYO" +
            "u54tgl9m05Axczu9TIcjEjHW6NbEudIGb7GzAkEAsToB5buAvXjb8qOShPs6D/sfQULC3//qO6Fe" +
            "mNI3RAFi7D2PGlh/QpjGVAhsORGYNy8j/9gAnnnH008lZGQXQQJAKTfFK7fH1UUES4Wo3rDZUzrz" +
            "a9eWGrjh1nWSFENFM20gqYla8G/lFSjgGJF/w877jjzKM95NSrPzQq1Wjt8+iQJAFnxQn1Ax3lhG" +
            "N7vPLDYfwMVQytvok7kJg/VOZj9NqcAvR9/rlyEhTFbL2v+Sk48K6/18KMrEEVdMJiBFkz4rwQJB" +
            "AL1/gyo+cYKGJo2Og3fohHzmycxbITjahyK98AeD3oS+ZjO6HReb2ZAnHTQMcxarhUujUu/lAGYf" +
            "5AHAxHa7apo=";

    public static final String MODULUSSTRING = "95701876885335270857822974167577168764621211406341574477817778908798408856077334510496515211568839843884498881589280440763139683446418982307428928523091367233376499779842840789220784202847513854967218444344438545354682865713417516385450114501727182277555013890267914809715178404671863643421619292274848317157";

    public static final String PUBLICEXPONENTSTRING = "65537";

    public static final String PRIVATEEXPONENTSTRING = "15118200884902819158506511612629910252530988627643229329521452996670429328272100404155979400725883072214721713247384231857130859555987849975263007110480563992945828011871526769689381461965107692102011772019212674436519765580328720044447875477151172925640047963361834004267745612848169871802590337012858580097";

    /**
    * 获取公钥和私钥
    * @return 返回的map中，包含公钥和私钥，可通过map.get("public")或者map.get("private")获取
    * @throws NoSuchAlgorithmException 使用JDK没有包含该加密算法
    * @author hey
    * @Date    2017年11月1日下午4:44:10
    * @version 1.00
    */
    public static HashMap<String, Object> getKeys() throws NoSuchAlgorithmException{  
        HashMap<String, Object> map = new HashMap<String, Object>();
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024);
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        //得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();  
        map.put("public", publicKey);  
        map.put("private", privateKey);  
        return map;  
    }

    /**
     * RSA公钥加密
     *
     * @param data
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encrypt( String data, String publicKey ) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(data.getBytes("UTF-8")));
        return outStr;
    }

    //将base64编码后的公钥字符串转成PublicKey实例
    public static RSAPublicKey getPublicKey(String publicKey) throws Exception{
        byte[ ] keyBytes=Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec=new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory=KeyFactory.getInstance("RSA");
        return (RSAPublicKey)keyFactory.generatePublic(keySpec);
    }

    //将base64编码后的私钥字符串转成PrivateKey实例
    public static RSAPrivateKey getPrivateKey(String privateKey) throws Exception{
        byte[ ] keyBytes=Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec=new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory=KeyFactory.getInstance("RSA");
        return (RSAPrivateKey)keyFactory.generatePrivate(keySpec);
    }

    /**
     * RSA私钥解密
     *
     * @param data
     *            加密字符串
     * @param privateKey
     *            私钥
     * @return 铭文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static String decrypt(String data, String privateKey) throws Exception{
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(data.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

	/**
	* 根据公钥或者私钥，得到它们的字符串类型
	* @param key 公钥或者私钥
	* @return 返回key的字符串类型
	* @throws Exception 转型时出现异常
	* @author hey
	* @Date    2017年11月1日下午4:47:19
	* @version 1.00
	*/
	public static String getKeyString(Key key) throws Exception {   
        byte[] keyBytes = key.getEncoded();   
        String s = (new BASE64Encoder()).encode(keyBytes);   
        return s;   
    }   
  
    /**
    * 使用模和指数生成RSA公钥 
    * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
    * @param modulus 模
    * @param exponent 指数
    * @return
    * @author hey
    * @Date    2017年11月1日下午4:46:49
    * @version 1.00
    */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {  
        try {  
            BigInteger b1 = new BigInteger(modulus);  
            BigInteger b2 = new BigInteger(exponent);  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);  
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
 
    /**
    * 使用模和指数生成RSA私钥 
    * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA 
    * /None/NoPadding】 
    * @param modulus 模
    * @param exponent 指数 
    * @return
    * @author hey
    * @Date    2017年11月1日下午4:48:45
    * @version 1.00
    */
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {  
        try {  
            BigInteger b1 = new BigInteger(modulus);  
            BigInteger b2 = new BigInteger(exponent);  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);  
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
  
    /**
    * 公钥加密 
    * @param data 需要加密的数据
    * @param publicKey 公钥
    * @return 返回加密后的密文
    * @throws Exception
    * @author hey
    * @Date    2017年11月1日下午4:49:05
    * @version 1.00
    */
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey)  
            throws Exception {  
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
        // 模长  
        int key_len = publicKey.getModulus().bitLength() / 8;  
        // 加密数据长度 <= 模长-11  
        String[] datas = StringUtils.splitString(data, key_len - 11);  
        String mi = "";  
        //如果明文长度大于模长-11则要分组加密  
        for (String s : datas) {  
            mi += StringUtils.bcd2Str(cipher.doFinal(s.getBytes()));  
        }  
        return mi;  
    }  
  
    /**
    * 私钥解密
    * @param data 需要解密的数据
    * @param privateKey 私钥
    * @return 返回解密后的明文
    * @throws Exception
    * @author hey
    * @Date    2017年11月1日下午4:49:29
    * @version 1.00
    */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey)  
            throws Exception {  
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);  
        //模长  
        int key_len = privateKey.getModulus().bitLength() / 8;  
        byte[] bytes = data.getBytes();  
        byte[] bcd = ASCII_To_BCD(bytes, bytes.length);  
//        System.err.println(bcd.length);  
        //如果密文长度大于模长则要分组解密  
        String ming = "";  
        byte[][] arrays = splitArray(bcd, key_len);  
        for(byte[] arr : arrays){  
            ming += new String(cipher.doFinal(arr));  
        }  
        return ming;  
    }  
 
    /**
    * 
    * @param ascii 
    * @param asc_len
    * @return
    * @author hey
    * @Date    2017年11月1日下午4:50:18
    * @version 1.00
    */
    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {  
        byte[] bcd = new byte[asc_len / 2];  
        int j = 0;  
        for (int i = 0; i < (asc_len + 1) / 2; i++) {  
            bcd[i] = asc_to_bcd(ascii[j++]);  
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));  
        }  
        return bcd;  
    }  
    private static byte asc_to_bcd(byte asc) {  
        byte bcd;  
  
        if ((asc >= '0') && (asc <= '9'))  
            bcd = (byte) (asc - '0');  
        else if ((asc >= 'A') && (asc <= 'F'))  
            bcd = (byte) (asc - 'A' + 10);  
        else if ((asc >= 'a') && (asc <= 'f'))  
            bcd = (byte) (asc - 'a' + 10);  
        else  
            bcd = (byte) (asc - 48);  
        return bcd;  
    }  
  
    /**
    * 拆分数组
    * @param data
    * @param len
    * @return
    * @author hey
    * @Date    2017年11月1日下午4:53:41
    * @version 1.00
    */
    public static byte[][] splitArray(byte[] data,int len){  
        int x = data.length / len;  
        int y = data.length % len;  
        int z = 0;  
        if(y!=0){  
            z = 1;  
        }  
        byte[][] arrays = new byte[x+z][];  
        byte[] arr;  
        for(int i=0; i<x+z; i++){  
            arr = new byte[len];  
            if(i==x+z-1 && y!=0){  
                System.arraycopy(data, i*len, arr, 0, y);  
            }else{  
                System.arraycopy(data, i*len, arr, 0, len);  
            }  
            arrays[i] = arr;  
        }  
        return arrays;  
    }

    /**
     * 获取随机盐值
     */
    public static String getRandomSalt(){
        return CommonUtils.getRandomString(6);
    }

}