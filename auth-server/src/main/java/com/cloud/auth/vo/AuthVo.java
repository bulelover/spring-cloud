package com.cloud.auth.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author SouthXia
 */
@Getter
@Setter
public class AuthVo implements Serializable {
    /**
     * 密钥
     */
    private String key;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}