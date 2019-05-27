package com.cloud.client.system.admin.controller;

import com.cloud.auth.entity.Res;
import com.cloud.auth.vo.AuthVo;
import com.cloud.client.system.user.feign.AuthFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Value("${auth.key}")
    private String key="";

    @Autowired
    private AuthFeign authFeign;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping("/main")
    public String login(){
        return "system/admin/login";
    }

    @PostMapping("/main/auth")
    @ResponseBody
    public Res auth(@RequestBody AuthVo vo){
        vo.setKey(key);
        Res res = authFeign.authenticate(vo);
        return res;
    }

    @RequestMapping("/admin")
    public String admin(){
        return "system/admin/index";
    }
}
