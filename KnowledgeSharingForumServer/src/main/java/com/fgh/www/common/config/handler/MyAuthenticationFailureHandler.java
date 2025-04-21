package com.fgh.www.common.config.handler;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.HashMap;

public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    /**
     * 当前用户认证失败后所执行的方法，一般用于返回登录失败信息
     * 自定义的登录认证失败处理器
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.out.println("错误信息：========"+exception.getMessage());

        HashMap<Object, Object> res = new HashMap<>();
        res.put("code", -1);
        res.put("msg", "登录失败");

        String json = JSON.toJSONString(res);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
    }
}
