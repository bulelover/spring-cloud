package com.cloud.client.system.user.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${server.port}")
    private String port;

    @RequestMapping("/list")
    public String list(){

        return "this is client service api";
    }
}
