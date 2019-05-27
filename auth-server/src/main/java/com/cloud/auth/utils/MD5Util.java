package com.cloud.auth.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Created by Xiann on 2017/10/12.
 */
public class MD5Util {
    public static String MD5(String str){
        return MD5(str,StandardCharsets.UTF_8);
    }

    public static String MD5(String inStr, Charset charset) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] byteArray = inStr.getBytes(charset);
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString().toUpperCase();
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
    }
    public static void main(String[] args) {
        System.out.print(MD5Util.MD5("gzxnh20161231"));
    }
}
