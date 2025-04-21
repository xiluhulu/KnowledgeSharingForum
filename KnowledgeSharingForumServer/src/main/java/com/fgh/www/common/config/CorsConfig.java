package com.fgh.www.common.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * 跨域配置
 */
/*@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 1 设置访问源地址
        corsConfiguration.addAllowedOrigin("*");
        // 2 设置访问源请求头
        corsConfiguration.addAllowedHeader("*");
        // 3 设置访问源请求方法
        corsConfiguration.addAllowedMethod("*");
        // 4 对接口配置跨域设置
        source.registerCorsConfiguration("/**", corsConfiguration);
        System.out.println("跨域配置成功");
        return new CorsFilter(source);
    }
}*/


/**
 * 跨域配置
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 动态设置访问源地址
        // 默认开发环境
        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://192.168.31.6:8080"));
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source) {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                String origin = request.getHeader("Origin");
                if (origin != null) {
                    corsConfiguration.setAllowedOrigins(Collections.singletonList(origin));
                }
                super.doFilterInternal(request, response, filterChain);
            }
        };
    }
}
