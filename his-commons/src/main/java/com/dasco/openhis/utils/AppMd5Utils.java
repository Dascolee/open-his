package com.dasco.openhis.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

import java.util.UUID;

/**
 * 生成md5密码
 */
public class AppMd5Utils {

    /**
     * 生成盐值
     * @return
     */
    public static String createSalt(){
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }

    /**
     * 生成md5密码
     * @param source
     * @param salt
     * @param hashIterators
     * @return
     */
   public static String md5(String source,String salt,Integer hashIterators){
        return new Md5Hash(source,salt,hashIterators).toString();
   }

    public static void main(String[] args) {
        String a = "001001";
    }
}
