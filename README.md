# spring-cloud
Spring-Cloud简单运用

Spring-Cloud 分布式微服务

- eureka注册中心 分布式服务注册中心
- 接口网关 zuul  负载均衡解决方案  也可作微服务统一拦截使用
- 熔断器 hystrix 解决服务雪崩
- config分布式配置中心（未使用）


简单的应用
- 使用AmazeUI作为前端UI框架
- Freemarker作为前端模板引擎
- 实现简单的分布式登录认证，Redis+Token （很抱歉，不会使用OAUTH2，那个更规范）
- 最终实现简单的CMS系统

说明
- cloud-discovery : 即 eureka注册中心，首先启动此项目 （localhost:8080/eureka/）
- eureka注册中心启动之后，依次启动zuul，auth-server，client 即可
- 接口网关配置的端口7010，访问地址：localhost:7010/client (路由到client服务)
- 注意更改数据库连接配置信息
