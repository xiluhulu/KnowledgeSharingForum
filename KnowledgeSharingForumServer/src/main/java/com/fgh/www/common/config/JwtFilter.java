package com.fgh.www.common.config;

import com.alibaba.fastjson2.JSON;
import com.fgh.www.common.util.Jwt_Utils;
import com.fgh.www.common.util.R;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author fgh
 */
public class JwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // 检查请求路径是否在允许放行的路径列表中
        if (isAllowedPath(path)) {
            filterChain.doFilter(request, response);
            return; // 直接返回，不进行后续JWT验证
        }
        String cookieToken = null;
        Cookie[] cookies = request.getCookies();
        if(cookies!=null && cookies.length>0){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    cookieToken = cookie.getValue();
                    break;
                }
            }
        }
        logger.info("token:" + cookieToken);
        String username = null;
        List<SimpleGrantedAuthority> authorities = null;
        if (cookieToken != null) {
            try {
                username = Jwt_Utils.getUserName(cookieToken);
                // 获取角色信息（假设JWT中有“roles”字段）
                List<String> roles = new ArrayList<>();
                roles.add(Jwt_Utils.getRole(cookieToken));
                System.out.println("role::::" + roles);
                if (roles != null) {
                    // 转换角色为GrantedAuthority
                    authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority(role))
                            .collect(Collectors.toList());
                }
            } catch (ExpiredJwtException e) {
                //在此帮我返回错误信息,设置响应状态码和响应体
                String json = JSON.toJSONString(R.error(4002, "登录已过期，请重新登录！"));
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().println(json);
                return; // 终止过滤器链
            } catch (Exception e) {
                logger.error("token解析错误: " + e.getMessage());
            }
        }
        // 如果解析出了用户名，并且 Spring Security 的上下文没有身份信息，创建一个新的 Authentication 对象
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);  // 继续请求处理
    }
    /**
     * 检查路径是否在允许放行的路径列表中
     *
     * @param path 请求路径
     * @return 是否允许放行
     */
    private boolean isAllowedPath(String path) {
        for (String allowedPath : WebSecurityConfig.VISITOR_IGNORE_URL) {
            if (allowedPath.contains("**")) {
                // 支持通配符模式
                String basePattern = allowedPath.replace("**", "(.*)");
                Pattern pattern = Pattern.compile("^" + basePattern + "$");
                if (pattern.matcher(path).matches()) {
                    return true;
                }
            } else if (allowedPath.equals(path)) {
                return true;
            }
        }
        return false;
    }
}
