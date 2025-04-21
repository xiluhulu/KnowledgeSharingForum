package com.fgh.www.service;

import com.fgh.www.pojo.Likes;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 点赞 服务类
 * </p>
 *
 * @author fgh
 * @since 2024-12-19
 */
public interface ILikesService extends IService<Likes> {


    void set(Likes likes, String token);
    //查询用户是否点赞
    boolean selectOne(Likes likes);
    //获取文章点赞数
    Long getArticleLikeCount(Integer fid);
    //取消点赞
    boolean deleteLike(Integer fid, Integer id);
}
