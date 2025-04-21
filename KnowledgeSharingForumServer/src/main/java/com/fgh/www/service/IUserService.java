package com.fgh.www.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.common.util.R;
import com.fgh.www.pojo.HeaderUserInfoVO;
import com.fgh.www.pojo.UpdatePasswordDTO;
import com.fgh.www.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fgh.www.vo.user.PersonPageGetUserInfoVO;

import java.util.List;

/**
 * <p>
 * 用户信息 服务类
 * </p>
 *
 * @author fgh
 * @since 2024-12-19
 */
public interface IUserService extends IService<User> {

    Page<User> adminSelectPage(User user, Integer pageNum, Integer pageSize);

    Page<User> selectPage(User user, Integer pageNum, Integer pageSize);



    HeaderUserInfoVO getUserInfo(String id);

    boolean adminUpdatePersonInfo(User user);
//    修改密码
    String updatePassword(UpdatePasswordDTO updatePasswordDTO, Integer id);

    String register(User user);

    //获取前端的头部用户信息
    com.fgh.www.vo.user.HeaderUserInfoVO getHeaderUserInfo(String username);

    //个人资料页面获取用户信息
    PersonPageGetUserInfoVO personPageGetUserInfo(Integer id);
    //个人资料修改个人信息
    boolean updatePersonInfo(User user);
//    超级管理员获取用户信息
    Page<User> superAdminSelectPage(User user, Integer pageNum, Integer pageSize);
//    超级管理员修改用户角色为普通用户
    boolean updateUserRoleToUser(User user);
//     超级管理员修改用户角色为管理员
    boolean updateUserRoleToAdmin(User user);

    // 注册用户

    R register1(User user);
    //设置vip
    boolean setVip(Integer id);
    //删除用户，其中包括用户的文章和评论
    boolean deleteById(Integer id);
    //批量删除用户
    boolean batchDelete(List<Integer> ids);
}
