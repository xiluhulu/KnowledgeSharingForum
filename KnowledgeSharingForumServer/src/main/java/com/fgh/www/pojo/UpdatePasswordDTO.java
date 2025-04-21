package com.fgh.www.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fgh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDTO {
    /**
     * 原始密码
     */
    private String password;
    /**
     * 新密码
     */
    private  String newPassword;
    /**
     * 重复密码
     */
    private String confirmPassword;
}
