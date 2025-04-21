package com.fgh.www.common.config;

import com.fgh.www.common.config.handler.MyAccessDeniedHandler;
import com.fgh.www.common.config.handler.MyAuthenticationEntryPoint;
import com.fgh.www.common.config.handler.MyAuthenticationFailureHandler;
import com.fgh.www.common.config.handler.MyAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.regex.Pattern;

/**
 * @author fgh
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)//开启方法权限控制
//@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true,securedEnabled = true)//开启方法权限控制
//@EnableWebSecurity//开启springSecurity自定义配置（springBoot项目可以省略）
public class WebSecurityConfig {

    /**
     * 当DBUserDetailsManager了中使用@Component就不需要再这里组成bean了否者会冲突
     */
//    @Bean
//    public UserDetailsService userDetailsService() {
//        //创建基于数据库的用户信息管理器
//        DBUserDetailsManager manager = new DBUserDetailsManager();
//
//        return manager;
//    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public PasswordEncoder passwordEncoder2() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    //放行swagger相关接口
    public static final String[] SWAGGER_IGNORE_URL = {"/swagger-ui.html", "/swagger-resources/**", "/v3/api-docs/**", "/webjars/**", "/doc.html"};
    //放行登录注册接口
    public static final String[] LOGIN_AND_REGISTER_IGNORE_URL = {"/login", "/register1"};
    //放行游客接口
    public static final String[] VISITOR_IGNORE_URL = {"/user-api/article/selectPage", "/user-api/article/selectById/**",
            "/user-api/category/selectAll", "/user-api/comment/selectForUser", "/user-api/comment/selectCount"
            , "/user-api/user/getArticleDetailAuthorData/**", "/user-api/likes/getLikeData/**"
            , "/user-api/favorites/getFavoriteData/**","/user-api/alipay/notify",
            "/user-api/advertisement/selectIndex","/user-api/advertisement/selectPrson"};



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {

            // 放行swagger相关接口
            auth.requestMatchers(SWAGGER_IGNORE_URL).permitAll()
                    // 放行登录接口
                    .requestMatchers(LOGIN_AND_REGISTER_IGNORE_URL).permitAll()
                    //放行文件上传接口
                    .requestMatchers("/files/**").permitAll()

                    //放行游客接口
                    .requestMatchers(VISITOR_IGNORE_URL).permitAll()
                    .requestMatchers("/admin-api/**").hasAnyAuthority("ADMIN","SUPERADMIN")
//                    .requestMatchers("/user/hello").hasRole("ADMIN")//该角色的用户可以访问此接口
//                    .requestMatchers("/url").hasAuthority("权限")//配置权限，有权限的用户可以访问此接口
                    .anyRequest()
                    .authenticated();
            //表单登录方式
        }).formLogin(form -> {

            form.disable();//禁用默认的登录页
            form.successHandler(new MyAuthenticationSuccessHandler());//配置自定义的登录成功认证处理器
            form.failureHandler(new MyAuthenticationFailureHandler());//配置自定义的登录失败认证处理器
        });
        http.exceptionHandling(exception -> {
            exception.authenticationEntryPoint(new MyAuthenticationEntryPoint());//配置自定义请求未认证的处理器
            exception.accessDeniedHandler(new MyAccessDeniedHandler());//配置自定义的权限不足处理器
        });
//        http.logout(logout->{
//            logout.logoutSuccessHandler(new MyLogoutSuccessHandler() );//配置自定义的退出成功处理器
//        });
        //关闭csrf攻击
        http.csrf(AbstractHttpConfigurer::disable);

        //开启跨域访问
        http.cors(Customizer.withDefaults());


        http.addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
