package com.fgh.www.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginInfoVO {
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 名称
     */
    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 角色标识
     */
    private String role;
    /**
     * 新密码
     */
    private String newPassword;
    /**
     * 头像
     */
    private String avatar;
    /**
     * token
     */
    private String token;
}
