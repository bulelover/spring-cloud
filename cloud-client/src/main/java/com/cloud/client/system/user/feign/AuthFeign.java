package com.cloud.client.system.user.feign;

import com.cloud.auth.entity.Res;
import com.cloud.auth.vo.AuthVo;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "auth-server", fallbackFactory = AuthFeignFallbackFactory.class)
public interface AuthFeign {

    @RequestMapping("/valid/{username}/{token}")
    public Res valid(@PathVariable("username") String username, @PathVariable("token") String token);

    @PostMapping("/authenticate")
    public Res authenticate(@RequestBody AuthVo vo);
}

@Component
class AuthFeignFallbackFactory implements FallbackFactory<AuthFeign>{
    private static final Logger logger = LoggerFactory.getLogger(AuthFeignFallbackFactory.class);

    @Override
    public AuthFeign create(Throwable throwable) {

        return new AuthFeign() {
            @Override
            public Res valid(String username,String token) {
                AuthFeignFallbackFactory.logger.info("AuthFeign.valid fallback; reason was:",throwable);
                return Res.failure("验证失败，服务拥挤或异常");
            }

            @Override
            public Res authenticate(AuthVo vo) {
                AuthFeignFallbackFactory.logger.info("AuthFeign.authenticate fallback; reason was:",throwable);
                return Res.failure("认证失败，服务拥挤或异常");
            }
        };
    }
}