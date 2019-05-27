package com.cloud.auth.controller;

import com.alibaba.fastjson.JSON;
import com.cloud.auth.entity.AuthUser;
import com.cloud.auth.entity.Res;
import com.cloud.auth.user.entity.SysUser;
import com.cloud.auth.user.service.ISysUserService;
import com.cloud.auth.utils.DateTools;
import com.cloud.auth.utils.TokenFactory;
import com.cloud.auth.vo.AuthVo;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class AuthController {

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Value("${system.user.password.secret}")
    private String secret;

    @Resource(name = "stringRedisTemplate")
    private RedisTemplate<String,String> userRt;

    @Autowired
    private ISysUserService sysUserService;


    private Ehcache getLoginHeartbeatCache() {
        return CacheManager.getCacheManager("authCache").getEhcache("login.heartbeat");
    }

    @PostMapping("/authenticate")
    public Res authenticate(@RequestBody AuthVo vo){
        AuthUser au = new AuthUser();
        //验证
        if(StringUtils.isBlank(vo.getUsername())){
            return Res.failure("用户名不能为空");
        }
        if(StringUtils.isBlank(vo.getPassword())){
            return Res.failure("密码不能为空");
        }

        Map<String,Object> param = new HashMap<>();
        param.put("username",vo.getUsername());
        //登录认证，连接数据库进行验证
        List<SysUser> suList = sysUserService.selectByMap(param);
        if(suList==null || suList.size() == 0){
            return Res.failure("用户不存在！");
        }
        SysUser su = suList.get(0);

        //密码编码器
        PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder(this.secret);
        if(!passwordEncoder.matches(vo.getPassword(),su.getPassword())){
            return Res.failure("密码不正确！");
        }

        //设置数据
        au.setIdNo(su.getIdNo());
        au.setUsername(vo.getUsername());
        au.setRealName(su.getRealName());
        au.setPhone(su.getPhone());
        au.setLoginTime(DateTools.getSysTime());

        //获取自定义token  作为 Key
        String tokenKey = TokenFactory.getTokenKey(vo.getUsername(),vo.getKey());
        String token = tokenKey.substring(tokenKey.lastIndexOf(":")+1);
        //校验key合法性
        int timeout = TokenFactory.getTimeout(token,vo.getUsername());
        if(timeout == 0){
            return Res.failure("传入KEY值不合法");
        }
        //生成token 对应数据 作为 Value
        String auJsonStr = JSON.toJSONString(au);
        //将Key Value 存入Redis 和Ehcache
        userRt.opsForValue().set(tokenKey,auJsonStr, timeout, TimeUnit.MINUTES);
        getLoginHeartbeatCache().put(new Element(tokenKey,auJsonStr));
        //返回数据
        Res res = Res.succeed(auJsonStr);
        res.setToken(token);
        return res;
    }

    /**
     * 验证登录状态，并刷新token密钥的存储过期时间
     * @param username 用户名
     * @param token 验证口令
     * @return 公共出参
     */
    @RequestMapping("/valid/{username}/{token}")
    public Res valid(@PathVariable("username") String username,
                     @PathVariable("token") String token){
        String tokenKey = TokenFactory.TOKEN_PRE+username+":"+token.toLowerCase();
        Element element = getLoginHeartbeatCache().get(tokenKey);
        String au = element == null ? null : element.getObjectValue().toString();
        //是否需要刷新 redis token时间
        boolean bool = false;
        if(StringUtils.isBlank(au)){
            bool = true;
            au = userRt.opsForValue().get(tokenKey);
        }
        if(StringUtils.isBlank(au)){
            return Res.failure("权限认证失败，此令牌不存在或已过期");
        }

        AuthUser authUser = JSON.parseObject(au,AuthUser.class);
        if(!username.equals(authUser.getUsername())){
            return Res.failure("权限认证失败，用户与认证信息不匹配");
        }

        //Ehcache token 每5~10分钟过期 之后 重新请求则刷新Redis和Ehcache Token
        if(bool){
            //刷新Redis --token
            userRt.opsForValue().set(tokenKey,au, TokenFactory.getTimeout(token,authUser.getUsername()), TimeUnit.MINUTES);
            logger.info("refresh redis login.heartbeat ==================>"+au);
            //刷新 Ehcache token
            getLoginHeartbeatCache().put(new Element(tokenKey,au));
        }

        //返回数据
        Res res = Res.succeed(au);
        res.setToken(token);
        return res;
    }
}
