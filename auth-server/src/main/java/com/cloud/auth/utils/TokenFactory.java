package com.cloud.auth.utils;

import com.alibaba.fastjson.JSON;
import com.cloud.auth.entity.AuthUser;
import org.springframework.util.Assert;

public class TokenFactory {

    //Redis密钥存储前缀
    public static final String TOKEN_PRE = "login:token:";
    //token加密口令
    private static final String TOKEN_SECURITY = "token_security";
    //session -token 较短的认证时间 无需密钥加密
    private static final int SESSION = 60;
    //session -token 较长的认证时间 需要密钥加密
    private static final int REFRESH_TOKEN_DAY = 60*24;
    //session -token 较长的认证时间 需要密钥加密
    public static final int REFRESH_TOKEN_WEEK = 60*24*7;


    //默认KEY
    private static final String SECURITY_KEY_DEFAULT = "__DEFAULT_KEY__";
    //其他配置KEY
    private static final String SECURITY_KEY = "TEST-KEY";

    public static String getTokenKey(String username){
        return getTokenKey(username,"");
    }

    public static String getTokenKey(String username, String apiKey){
        if (username == null) {
            throw new NullPointerException("null username is illegal");
        }

        apiKey = apiKey==null?SECURITY_KEY_DEFAULT:apiKey;
        return TOKEN_PRE + username+":"+MD5Util.MD5(TOKEN_SECURITY+apiKey+username).toLowerCase();
    }

    /**
     * 返回 超时分钟数
     * @return 超时分钟数
     */
    public static int getTimeout(String token, String username){
        Assert.hasText(token,"token不能为空");
        Assert.notNull(username,"user不能为null");
        if(token.equalsIgnoreCase(MD5Util.MD5(TOKEN_SECURITY + SECURITY_KEY_DEFAULT + username))){
            return SESSION;
        }
        if(token.equalsIgnoreCase(MD5Util.MD5(TOKEN_SECURITY + SECURITY_KEY + username))){
            return REFRESH_TOKEN_DAY;
        }
        return 0;
    }
}
