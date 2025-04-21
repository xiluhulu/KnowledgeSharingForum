package com.fgh.www.common.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
public class WebConfig implements  WebMvcConfigurer {



    // 加自定义拦截器JwtInterceptor，设置拦截规则
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/login")
                .excludePathPatterns("/register")
                .excludePathPatterns("/user/getUserInfo")
                .excludePathPatterns("/article/updateReadCount")
                .excludePathPatterns("/comment/selectForUser")
                .excludePathPatterns("/article/selectById/**")
                .excludePathPatterns("/comment/selectCount")
                .excludePathPatterns("/category/selectAll")
                .excludePathPatterns("/article/selectPage")
                .excludePathPatterns("/article/selectHourRank")
                .excludePathPatterns("/files/**");*/
    }
}
