package com.fgh.www.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeaderUserInfoVO {
    /**
     * ID
     */
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
     * 头像
     */
    private String avatar;
}
