package com.fgh.www.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.common.config.DBUserDetailsManager;

import com.fgh.www.common.util.R;
import com.fgh.www.mapper.ArticleMapper;
import com.fgh.www.mapper.CommentMapper;
import com.fgh.www.pojo.*;
import com.fgh.www.mapper.UserMapper;
import com.fgh.www.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fgh.www.vo.user.PersonPageGetUserInfoVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author fgh
 * @since 2024-12-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private DBUserDetailsManager dbUserDetailsManager;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private PasswordEncoder passwordEncoder2;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Override
    public Page<User> adminSelectPage(User user, Integer pageNum, Integer pageSize) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.and(w->{
           w.eq("role", "ADMIN");
        });
        if(StringUtils.isNoneBlank(user.getUsername())){
            wrapper.like("username", user.getUsername());
        }
        return this.page(new Page<User>(pageNum, pageSize), wrapper);
    }

    @Override
    public Page<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.and(w->{
            w.eq("role", "USER");
        });
        if(StringUtils.isNoneBlank(user.getUsername())){
            wrapper.like("username", user.getUsername());
        }
        if(StringUtils.isNoneBlank(user.getName())){
            wrapper.like("name", user.getName());
        }
        return this.page(new Page<User>(pageNum, pageSize), wrapper);
    }



    @Override
    public HeaderUserInfoVO getUserInfo(String id) {
        MPJLambdaWrapper<User> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(User.class).eq(User::getId, id);
        User user = baseMapper.selectOne(wrapper);

        return new HeaderUserInfoVO(user.getId(),user.getUsername(), user.getName(), user.getAvatar());
    }

    @Override
    public boolean adminUpdatePersonInfo(User user) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(User::getName, user.getName())
                        .set(User::getAvatar, user.getAvatar())
                                .set(User::getEmail, user.getEmail())

                                                .eq(User::getId, user.getId());
        int update = baseMapper.update(wrapper);
        return update != 0;
    }
    //    修改密码
    @Transactional
    @Override
    public String updatePassword(UpdatePasswordDTO updatePasswordDTO ,Integer id) {
        User user = baseMapper.selectById(id);
        // 2. 验证旧密码（使用加密验证）

        if (!passwordEncoder2.matches(updatePasswordDTO.getPassword(), user.getPassword())) {
            return "原密码不正确";
        }
        if(!updatePasswordDTO.getNewPassword().equals(updatePasswordDTO.getConfirmPassword())){
            return "两次密码不正确";
        }

        // 4. 更新数据库密码
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder().username(user.getUsername()).password(updatePasswordDTO.getNewPassword()).build();
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(User::getPassword, userDetails.getPassword())
                .eq(User::getId, id);
        int update = baseMapper.update(wrapper);

        return update != 0 ? "修改成功" :"修改失败";
    }

    @Override
    public String register(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        List<User> users = baseMapper.selectList(wrapper);
        if (!users.isEmpty()){
            return "用户已存在";
        }

        int insert = baseMapper.insert(user);
        if (insert==0){
            return "注册失败";
        }
        return "注册成功";
    }

    @Override
    public com.fgh.www.vo.user.HeaderUserInfoVO getHeaderUserInfo(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = baseMapper.selectOne(wrapper);
        return new com.fgh.www.vo.user.HeaderUserInfoVO(user.getName(),user.getAvatar());
    }
    /**
     * 个人中心页面获取用户信息
     * @param id
     * @return
     */
    @Override
    public PersonPageGetUserInfoVO personPageGetUserInfo(Integer id) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, id);
        User user = baseMapper.selectOne(wrapper);
        return new PersonPageGetUserInfoVO(user.getId(),user.getAvatar(),user.getUsername(),user.getName(),user.getInfo(),user.getEmail(),user.getVipExpirationTime());
    }

    @Override
    public boolean updatePersonInfo(User user) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, user.getId())
                .set(User::getName, user.getName())
                .set(User::getAvatar, user.getAvatar())
                .set(User::getEmail, user.getEmail())
                .set(User::getInfo, user.getInfo());
        int update = baseMapper.update(wrapper);
        if (update != 0){
            return true;
        }
        return false;
    }
//    超级管理员查询用户信息
    @Override
    public Page<User> superAdminSelectPage(User user, Integer pageNum, Integer pageSize) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.and(w->{
            w.eq("role", "SUPERADMIN");
        });
        if(StringUtils.isNoneBlank(user.getUsername())){
            wrapper.like("username", user.getUsername());
        }
        return this.page(new Page<User>(pageNum, pageSize), wrapper);
    }
//    超级管理员修改用户角色为普通用户
    @Override
    public boolean updateUserRoleToUser(User user) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<User>()
                .eq(User::getId, user.getId())
                .set(User::getRole, "USER");
        int update = baseMapper.update(wrapper);
        return update != 0;
    }
//    超级管理员修改用户角色为管理员
    @Override
    public boolean updateUserRoleToAdmin(User user) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<User>()
                .eq(User::getId, user.getId())
                .set(User::getRole, "ADMIN");
        int update = baseMapper.update(wrapper);
        return update != 0;
    }
    @Transactional
    @Override
    public R register1(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        User user1 = baseMapper.selectOne(wrapper);
        if (user1!=null){
            return R.error("用户已存在");
        }
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder().username(user.getUsername()).password(user.getPassword()).build();
        dbUserDetailsManager.createUser(userDetails);
        return R.ok("注册成功");
    }
    //设置vip
    @Override
    public boolean setVip(Integer id) {
//        判断是否会员
        User user = baseMapper.selectById(id);
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        if (user.getVipStatus()==1){
//        会员进行去除会员
                        wrapper.eq(User::getId,id)
                                .set(User::getVipStatus,0)
                                .set(User::getVipExpirationTime,null);
        }else {
            String nextMonth = DateUtil.format(DateUtil.nextMonth(), "yyyy-MM-dd");
            wrapper.eq(User::getId,id)
                    .set(User::getVipStatus,1)
                    .set(User::getVipExpirationTime,nextMonth);
        }

//        非会员变成会员
        int update = baseMapper.update(wrapper);
        return update>0;
    }
    //删除用户包括用户的文章和评论
    @Transactional
    @Override
    public boolean deleteById(Integer id) {
        int i = baseMapper.deleteById(id);
//        删除文章
        LambdaUpdateWrapper<Article> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Article::getUserId,id)
                .set(Article::getDeleted,1);
        articleMapper.update(wrapper);
//        删除评论
        LambdaUpdateWrapper<Comment> wrapper1 = new LambdaUpdateWrapper<>();
        wrapper1.eq(Comment::getUserId,id)
                        .set(Comment::getDeleted,1);
        commentMapper.update(wrapper1);
        return i>0;
    }

    /**
     * 批量删除用户
     *
     * @param ids
     * @return
     */
    @Transactional
    @Override
    public boolean batchDelete(List<Integer> ids) {
        Integer i=0;
        for (Integer item:ids){
            boolean b = this.deleteById(item);
            if(b){
                i+=1;
            }
        }
        System.out.println(i==ids.size());
        return i==ids.size();
    }

    //    定时任务，更新vip状态
    @Scheduled(cron = "0 10 0 * * ?")//每天0点10分执行一次
//    @Scheduled(fixedRate = 1 * 60 * 1000)//每分钟执行一次
    public void updateVip(){
        DateTime yesterday = DateUtil.yesterday();
        String dateStr = yesterday.toDateStr();
        System.out.println("---------------"+dateStr);
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getVipExpirationTime, dateStr)
                .set(User::getVipStatus, 0)
                .set(User::getVipExpirationTime, null);
        int update = baseMapper.update(wrapper);
        System.out.println("vip过期了"+update+"人");
    }
}
