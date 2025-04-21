package com.fgh.www.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.common.util.Jwt_Utils;
import com.fgh.www.common.util.R;
import com.fgh.www.pojo.UpdatePasswordDTO;
import com.fgh.www.pojo.User;
import com.fgh.www.service.IArticleService;
import com.fgh.www.service.IUserService;
import com.fgh.www.vo.user.HeaderUserInfoVO;
import com.fgh.www.vo.user.PersonPageGetUserInfoVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@RequestMapping("/admin-api/user")
public class AUserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IArticleService articleService;
    /**
     * 获取头部的用户信息
     * 需要登录
     * @return
     */
    @GetMapping("/getHeaderUserInfo")
    public R getHeaderUserInfo() {
        //控制器中获取用户身份信息
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        //获取用户名
        String username = (String) authentication.getPrincipal();
        System.out.println("账号======>>>>:::"+username);
        HeaderUserInfoVO headerUserInfo = userService.getHeaderUserInfo(username);
        return R.ok().put("data", headerUserInfo);
    }
    /**
     * 个人中心获取用户简介信息
     * 需要登录
     * @return
     */
    @GetMapping("/personPageGetUserInfo")
    public R personPageGetUserInfo(@CookieValue("token")String token) {
        Integer id = Jwt_Utils.getId(token);
        PersonPageGetUserInfoVO personInfoVO = userService.personPageGetUserInfo(id);
        return R.ok().put("data", personInfoVO);
    }
    /**
     * 个人中心修改用户简介信息
     * 需要登录
     * @return
     */
    @PutMapping("/updatePersonInfo")
    public R updatePersonInfo(@RequestBody User user) {
        boolean b = userService.updatePersonInfo(user);
        if (b) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 分页查询
     */
    @GetMapping("/adminSelectPage")
    public R adminSelectPage(User user,
                             @RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum,
                             @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize) {
        Page<User> userPage = userService.adminSelectPage(user, pageNum, pageSize);
        return R.ok("成功").put("data", userPage);
    }
    /**
     * 普通用户：分页查询
     */
    @GetMapping("/selectPage")
    public R selectPage(User user,
                        @RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum,
                        @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize) {
        Page<User> userPage = userService.selectPage(user, pageNum, pageSize);
        return R.ok("成功").put("data", userPage);
    }
    /**
     * 修改
     */
    @PutMapping("/update")
    public R updateById(@RequestBody User user) {
        boolean b = userService.updateById(user);
        if (!b){
            return R.error();
        }
        return R.ok();
    }
    /**
     * 分页查询
     */
    @GetMapping("/superAdminSelectPage")
    public R superAdminSelectPage(User user,
                             @RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum,
                             @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize) {
        Page<User> userPage = userService.superAdminSelectPage(user, pageNum, pageSize);
        return R.ok("成功").put("data", userPage);
    }
    //超级管理员修改用户角色为普通用户
    @PutMapping("/updateUserRoleToUser")
    public R updateUserRoleToUser(@RequestBody User user) {
        boolean b = userService.updateUserRoleToUser(user);
        if (!b){
            return R.error();
        }
        return R.ok();
    }
    //超级管理员修改用户角色为管理员
    @PutMapping("/updateUserRoleToAdmin")
    public R updateUserRoleToAdmin(@RequestBody User user) {
        boolean b = userService.updateUserRoleToAdmin(user);
        if (!b){
            return R.error();
        }
        return R.ok();
    }
    //获取用户角色
    @GetMapping("/getUserRole")
    public R getUserRole(@CookieValue("token") String token) {
        Integer id = Jwt_Utils.getId(token);
        User user = userService.getById(id);
        if ("SUPERADMIN".equals(user.getRole())){
            return R.ok().put("data",true);
        }
        return R.error();
    }
    //修改密码
    @PutMapping("/updatePassword")
    public R updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO, @CookieValue("token")String token){
        Integer id = Jwt_Utils.getId(token);
        String s = userService.updatePassword(updatePasswordDTO, id);

        if ("原密码不正确".equals(s)||"修改失败".equals(s) || "两次密码不正确".equals(s)){
            return R.error(s);
        }
        return R.ok();
    }
    @PutMapping("/setVip")
    public R setVip(@RequestBody User user){
        boolean b = userService.setVip(user.getId());
        if(b){
            return R.ok();
        }
        return R.error("出现异常，请联系技术人员");
    }
    @PostMapping("/loginCheck")
    public R loginCheck(@CookieValue("token")String token){
        Integer id = Jwt_Utils.getId(token);
        User user = userService.getById(id);
        return R.ok("登录成功").put("userId",user.getId()).put("avatar",user.getAvatar()).put("username",user.getUsername()).put("name",user.getName())
                .put("vipExpirationTime",user.getVipExpirationTime()).put("info",user.getInfo()).put("email",user.getEmail());
    }
    @DeleteMapping("/delete/{id}")
    public R deleteById(@PathVariable(name = "id") Integer id) {
        boolean b = userService.deleteById(id);
//        boolean b = userService.removeById(id);

        return b ? R.ok("删除成功") : R.error();
    }
    @DeleteMapping("/delete/batch")
    public R batchDelete(@RequestBody List<Integer> ids) {
        boolean b = userService.batchDelete(ids);
//        boolean b = userService.removeById(id);

        return b ? R.ok("删除成功") : R.error();
    }
//    修改用户信息
    @PutMapping("/adminUpdate")
    public R adminUpdate(@RequestBody User user){
        boolean b = userService.updateById(user);
        if(!b){
            return R.error();
        }
        return R.ok();
    }
}
