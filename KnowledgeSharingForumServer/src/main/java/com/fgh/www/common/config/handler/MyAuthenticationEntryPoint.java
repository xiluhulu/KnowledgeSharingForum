package com.fgh.www.common.config.handler;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.HashMap;

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * 当前用户的请求未认证所执行的方法，一般用于返回请求未认证信息
     * 自定义的请求未认证处理器
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        System.out.println("错误信息：========"+authException.getMessage());

        HashMap<Object, Object> res = new HashMap<>();
        res.put("code", -1);
        res.put("msg", "请登录！");

        String json = JSON.toJSONString(res);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
    }
}
