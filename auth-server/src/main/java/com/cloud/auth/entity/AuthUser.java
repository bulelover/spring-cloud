package com.cloud.auth.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AuthUser implements Serializable {
    private String id;
    private String username;
    private String realName;
    private String idNo;
    private String phone;
    private String password;
    private String loginTime;
}
