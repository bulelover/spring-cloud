package com.cloud.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 加入 @EnableDiscoveryClient注解，声明该微服务注册到服务注册中心。
 * 加入 @EnableFeignClients，声明使用Feign调用接口。
 * 加入 @EnableHystrix  启用熔断器
 * 加入 @EnableHystrixDashboard  启用熔断器监控
 * 加入 @ServletComponentScan  启用Servlet注解扫描 扫描监听器 过滤器
 */
@ServletComponentScan
@EnableFeignClients
@EnableHystrix
@EnableHystrixDashboard
@EnableDiscoveryClient
@SpringBootApplication
public class CloudClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudClientApplication.class, args);
    }
}
