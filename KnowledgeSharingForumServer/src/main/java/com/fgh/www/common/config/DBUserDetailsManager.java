package com.fgh.www.common.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fgh.www.mapper.UserMapper;
import com.fgh.www.pojo.User;
import jakarta.annotation.Resource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author fgh
 */
@Component
public class DBUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {
    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        User user = userMapper.selectOne(userQueryWrapper);
        if(user==null){
            System.out.println("用户名不存在");
            throw new UsernameNotFoundException("用户名不存在");
        }else {
            System.out.println("用户名存在");
            System.out.println(user);
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())//用户名
                    .password(user.getPassword())//密码
                    .disabled(false)//用户账号是否禁用
                    .credentialsExpired(false)//用户凭证是否过期
                    .accountLocked(false)//用户是否被锁定
                    .roles(user.getRole())//角色
                    .build();
        }

    }
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return user;
    }

    @Override
    public void createUser(UserDetails user) {
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        String role="USER";
        for (GrantedAuthority authority : authorities) {
            role = authority.getAuthority();
        }
        userMapper.insert(new User(null,user.getUsername(),user.getPassword(),null,null,role,null,null,null,null,null));
    }

    @Override
    public void updateUser(UserDetails user) {
    }
    @Override
    public void deleteUser(String username) {
    }
    @Override
    public void changePassword(String oldPassword, String newPassword) {
    }
    @Override
    public boolean userExists(String username) {
        return false;
    }
    /**
     * 从数据库中获取用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */

}
