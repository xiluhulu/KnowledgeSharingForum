package com.fgh.www.service;

import com.fgh.www.pojo.Follow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 关注表 服务类
 * </p>
 *
 * @author fgh
 * @since 2025-02-19
 */
public interface IFollowService extends IService<Follow> {
    //查询是否已经关注
    boolean selectOne(Follow follow);
    //取消关注
    boolean deleteFollow(Integer id, Integer followingId);
}
