package com.app.tools;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 安全工具类
 */
public class SecurityUtil {

    /**
     * md5 1次加密
     */
    public static String md5Hex(String value){
        return DigestUtils.md5Hex(value);
    }

    /**
     * 3次加密
     * @param value
     * @return
     */
    public static String md5Hex3(String value){
        for (int i = 0; i < 3; i++) {
             value = DigestUtils.md5Hex(value);
        }
        return value;
    }

    /**
     * 256 位加密
     * @param value
     * @return
     */
    public static String sha256Hex(String value){
        return DigestUtils.sha256Hex(value);
    }
    /**
     * 512 位加密
     * @param value
     * @return
     */
    public static String sha512Hex(String value){
        return DigestUtils.sha512Hex(value);
    }
}
