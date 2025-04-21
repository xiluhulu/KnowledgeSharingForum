package com.fgh.www.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author fgh
 * @since 2024-12-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField(value = "username", condition = "UNIQUE")
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 角色标识
     */
    private String role;





    /**
     * 邮箱
     */
    private String email;

    /**
     * 简介
     */
    private String info;



    //    deleted逻辑删除1为删除
    @TableLogic
    private Integer deleted;
    //    vip状态1为vip，0为普通用户
    private Integer vipStatus;
    //    vip过期时间
    private String vipExpirationTime;
}
