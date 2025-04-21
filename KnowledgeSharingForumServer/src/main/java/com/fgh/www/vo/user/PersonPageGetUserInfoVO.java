package com.fgh.www.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonPageGetUserInfoVO {
    /**
     * id
     */
    private Integer id;

    /**
     * 头像
     */
    private String avatar;
    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String name;
    /**
     * 简介
     */
    private String info;
    /**
     * 邮箱
     */
    private String email;

    /**
     *  vip过期时间
     */
    private String vipExpirationTime;
}
