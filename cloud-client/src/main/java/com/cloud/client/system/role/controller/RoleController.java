package com.cloud.client.system.role.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author SouthXia 2019.7.17
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    @RequestMapping("/list")
    public String list(){
        return "system/admin/role/list";
    }
}
