package com.fgh.www.common.config.handler;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    /**
     * 当用户认证成功后所执行的方法，一般用于返回登录成功信息
     * 自定义的登录成功认证处理器
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();//获取用户身份信息
        Object credentials = authentication.getCredentials();//获取用户凭证信息
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();//获取用户权限信息
        System.out.println("用户身份信息：========"+principal);
        System.out.println("用户凭证信息：========"+credentials);
        System.out.println("用户权限信息：========"+authorities);
        HashMap<Object, Object> res = new HashMap<>();
        res.put("code", 0);
        res.put("msg", "登录成功");
        res.put("data", principal);
        String json = JSON.toJSONString(res);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
    }
}
