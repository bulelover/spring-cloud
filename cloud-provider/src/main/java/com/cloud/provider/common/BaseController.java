package com.cloud.provider.common;

import com.alibaba.fastjson.JSON;
import com.cloud.auth.entity.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @author SouthXia 19.8.16
 */
public class BaseController {
    @Autowired
    protected HttpServletRequest request;

    private AuthUser loginer;

    protected AuthUser getLoginer(){
        if(loginer != null){
            return loginer;
        }
        this.setLoginer((AuthUser) request.getAttribute("loginer"));
        return loginer;
    }

    private void setLoginer(AuthUser loginer) {
        this.loginer = loginer;
    }
}
