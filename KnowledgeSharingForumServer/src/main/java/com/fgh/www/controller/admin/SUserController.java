package com.fgh.www.controller.admin;


import com.fgh.www.common.util.Jwt_Utils;
import com.fgh.www.common.util.R;
import com.fgh.www.pojo.User;
import com.fgh.www.service.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author fgh
 * @since 2024-12-19
 */
@Tag(name = "后台User接口", description = "包含用户相关的 API 接口")
@RestController
@RequestMapping("/admin-api/super")
public class SUserController {
    @Autowired
    private IUserService userService;
    //超级管理员修改用户角色为普通用户
    @PutMapping("/updateUserRoleToUser")
    public R updateUserRoleToUser(@RequestBody User user ,@CookieValue("token") String token) {
        String role = Jwt_Utils.getRole(token);
        if (!"SUPERADMIN".equals(role)){
            return R.error("你没有此权限");
        }
        boolean b = userService.updateUserRoleToUser(user);
        if (!b){
            return R.error();
        }
        return R.ok();
    }
    //超级管理员修改用户角色为管理员
    @PutMapping("/updateUserRoleToAdmin")
    public R updateUserRoleToAdmin(@RequestBody User user,@CookieValue("token") String token) {
        String role = Jwt_Utils.getRole(token);
        if (!"SUPERADMIN".equals(role)){
            return R.error("你没有此权限");
        }
        boolean b = userService.updateUserRoleToAdmin(user);
        if (!b){
            return R.error();
        }
        return R.ok();
    }
    @GetMapping("/getUserRole")
    public R getUserRole(@CookieValue("token") String token) {
        Integer id = Jwt_Utils.getId(token);
        User user = userService.getById(id);
        if ("SUPERADMIN".equals(user.getRole())){
            return R.ok().put("data",true);
        }
        return R.error();
    }
}
