package com.fgh.www.controller.user;


import com.fgh.www.common.util.Jwt_Utils;
import com.fgh.www.common.util.R;
import com.fgh.www.pojo.UpdatePasswordDTO;
import com.fgh.www.pojo.User;
import com.fgh.www.service.IArticleService;
import com.fgh.www.vo.user.ArticleDetailAuthorData;
import com.fgh.www.service.IUserService;
import com.fgh.www.vo.user.HeaderUserInfoVO;
import com.fgh.www.vo.user.PersonPageGetUserInfoVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author fgh
 * @since 2024-12-19
 */
@Tag(name = "前端User接口", description = "包含用户相关的 API 接口")
@RestController
@RequestMapping("/user-api/user")
public class UUserController {
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
    public R updatePersonInfo(@RequestBody User user ,@CookieValue("token")String token) {
        Integer id = Jwt_Utils.getId(token);
        user.setId(id);
        boolean b = userService.updatePersonInfo(user);
        if (b) {
            return R.ok();
        }
        return R.error();
    }
    /**
     * 详情页获取作者信息数据
     * 不需要登录
     * @return
     */
    @GetMapping("/getArticleDetailAuthorData/{authorId}")
    public R getArticleDetailAuthorData(@PathVariable("authorId")Integer authorId) {
        ArticleDetailAuthorData articleDetailAuthorData = articleService.getArticleDetailAuthorData(authorId);
        return R.ok().put("data", articleDetailAuthorData);
    }
    @GetMapping("getVipStatus")
    public R getVipStatus(@CookieValue("token")String token) {
        Integer id = Jwt_Utils.getId(token);
        User user = userService.getById(id);
        if(user.getVipStatus()==1){
            return R.ok().put("data", true);
        }
        return R.ok().put("data", false);
    }
    @PutMapping("/updatePassword")
    public R updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO, @CookieValue("token")String token){
        Integer id = Jwt_Utils.getId(token);
        String s = userService.updatePassword(updatePasswordDTO, id);

        if ("原密码不正确".equals(s)||"修改失败".equals(s) || "两次密码不正确".equals(s)){
            return R.error(s);
        }
        return R.ok();
    }
    @PostMapping("/loginCheck")
    public R loginCheck(@CookieValue("token")String token){
        Integer id = Jwt_Utils.getId(token);
        User user = userService.getById(id);
        if(ObjectUtils.isEmpty(user)){
            return R.error(499,"用户不存在，请重新登录！");
        }
        return R.ok("登录成功").put("userId",user.getId()).put("avatar",user.getAvatar()).put("username",user.getUsername()).put("name",user.getName())
                .put("vipExpirationTime",user.getVipExpirationTime()).put("info",user.getInfo()).put("email",user.getEmail());
    }
}
