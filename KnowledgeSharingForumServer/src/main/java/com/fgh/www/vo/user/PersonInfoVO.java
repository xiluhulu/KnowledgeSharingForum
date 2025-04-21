package com.fgh.www.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonInfoVO {
    /**
     * 用户名
     */
    private String username;
    /**
     * 姓名
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;


    /**
     * 邮箱
     */
    private String email;
}
