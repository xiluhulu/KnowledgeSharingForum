package com.fgh.www.common.config.handler;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.HashMap;

public class MyAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * 当前用户的无权限是执行的方法，一般用于返回无权限提醒信息
     * 自定义无权限提醒信息处理器
     * @param request
     * @param response
     * @param accessDeniedException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        System.out.println("错误信息：========"+accessDeniedException.getMessage());

        HashMap<Object, Object> res = new HashMap<>();
        res.put("code", -1);
        res.put("msg", "没有权限");

        String json = JSON.toJSONString(res);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
    }
}
