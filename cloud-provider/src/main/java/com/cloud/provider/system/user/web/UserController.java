package com.cloud.provider.system.user.web;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cloud.auth.entity.Res;
import com.cloud.provider.system.user.entity.User;
import com.cloud.provider.system.user.service.IUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author SouthXia
 * @since 2019-08-02
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/query")
    @ResponseBody
    public Res query(@RequestBody User entity){
        Page<User> page = new Page<>(entity.getPageCurrent(), entity.getPageSize());
        Wrapper<User> wrapper = new EntityWrapper<>();
        wrapper.like(!StringUtils.isEmpty(entity.getPageSearch()),"real_name",entity.getPageSearch())
        .or().like(!StringUtils.isEmpty(entity.getPageSearch()),"id_no",entity.getPageSearch())
        .orderBy("realName".equals(entity.getPageOrderField()),"real_name",entity.getPageOrder())
        .orderBy("id");
        int count = userService.selectCount(wrapper);
        page= userService.selectPage(page,wrapper);
        page.setTotal(count);
        return Res.success(page);
    }
}
