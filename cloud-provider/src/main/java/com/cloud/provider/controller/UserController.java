package com.cloud.provider.controller;
import com.cloud.provider.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Value("${server.port}")
    private String port;

    @RequestMapping("/getProviderServiceApi")
    public String getProviderServiceApi(){
        return "this is provider service api";
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        User findOne = new User();
        if (id == 1) {
            findOne.setAge(20);
            findOne.setUsername("zhangsan");
            findOne.setId(1L);
            findOne.setBalance(800D);
        }else if (id == 2) {
            findOne.setAge(27);
            findOne.setUsername("xianannan");
            findOne.setId(13L);
            findOne.setBalance(800D);
        } else {
            findOne.setAge(18);
            findOne.setUsername("lisi");
            findOne.setId(2L);
            findOne.setBalance(2000D);
        }
        findOne.setName(port);
        return findOne;
    }
}
