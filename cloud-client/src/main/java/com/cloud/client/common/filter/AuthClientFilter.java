package com.cloud.client.common.filter;

import com.alibaba.fastjson.JSON;
import com.cloud.auth.entity.AuthUser;
import com.cloud.auth.entity.Res;
import com.cloud.client.system.user.feign.AuthFeign;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 权限认证过滤器
 * @author SouthXia
 */
@WebFilter(filterName = "authClientFilter", urlPatterns = "/*")
public class AuthClientFilter implements Filter {

    @Autowired
    private AuthFeign authFeign;

    private Logger logger = LoggerFactory.getLogger(AuthClientFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("AuthFilter init...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getServletPath();
        //静态文件
        if(requestURI.startsWith("/static")){
            filterChain.doFilter(request, servletResponse);
            return;
        }
        //登录跳转
        if("/".equals(requestURI) || "/login".equals(requestURI)){
            request.getRequestDispatcher("/main").forward(request,response);
            return;
        }
        //无需验证token的URL过滤  登录
        if(requestURI.startsWith("/main")){
            filterChain.doFilter(request, servletResponse);
            return;
        }

        //token 验证
        String token = request.getParameter("token");
        String username = request.getParameter("username");
        Res res;

        if(StringUtils.isBlank(token) || StringUtils.isBlank(username)){
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = response.getWriter();
            res = new Res();
            res.setErrorMsg("param 'token' and 'username' is required ...");
            res.setFlag(0);
            out.append(JSON.toJSONString(res));
            return;
        }
        //验证和刷新token缓存
        res = authFeign.valid(username,token);
        if(res.getFlag() == 0){
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = response.getWriter();
            out.append(JSON.toJSONString(res));
            return;
        }
        //解码
        Res.decode(res);
        request.setAttribute("loginer",JSON.parseObject(res.getResult(), AuthUser.class));
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {
        logger.info("AuthFilter destroy...");
    }
}
