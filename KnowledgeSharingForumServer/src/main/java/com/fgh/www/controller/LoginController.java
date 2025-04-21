package com.fgh.www.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.fgh.www.common.util.Jwt_Utils;
import com.fgh.www.common.util.R;
import com.fgh.www.pojo.User;
import com.fgh.www.service.IUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private IUserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @PostMapping("/login")
    public R login(@RequestBody User user, HttpServletResponse response) {
        User user1 = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        if(user1==null){
            return R.error("用户名不存在");
        }
        String[] split = user1.getPassword().split("}");
        String password = split[1];

        if (bCryptPasswordEncoder.matches(user.getPassword(), password)){
            System.out.println("role:::::->"+user1.getRole());
            if(user.getRole()!=null){
                if(!("SUPERADMIN".equals(user1.getRole()) || "ADMIN".equals(user1.getRole()))) {
                    return R.error("权限不足");
                }
            }
            String token = Jwt_Utils.createToken(user1.getId(), user1.getUsername(), user1.getRole());
            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(Integer.parseInt(Jwt_Utils.TIME.toString()));
            response.addCookie(cookie);
            return R.ok("登录成功").put("token",token).put("expires",Jwt_Utils.TIME).put("userId",user1.getId())
                    .put("avatar",user1.getAvatar()).put("username",user1.getUsername()).put("name",user1.getName())
                    .put("vipExpirationTime",user1.getVipExpirationTime()).put("info",user1.getInfo()).put("email",user1.getEmail());
        }else {
            return R.error("密码错误");
        }

    }
    @PostMapping("/register1")
    public R register(@RequestBody User user) {

        R r = userService.register1(user);
        return r;
    }

}
