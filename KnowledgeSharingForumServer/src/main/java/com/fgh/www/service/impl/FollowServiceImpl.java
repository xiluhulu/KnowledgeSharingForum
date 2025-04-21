package com.fgh.www.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fgh.www.pojo.Follow;
import com.fgh.www.mapper.FollowMapper;
import com.fgh.www.service.IFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 关注表 服务实现类
 * </p>
 *
 * @author fgh
 * @since 2025-02-19
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {

    @Override
    public boolean selectOne(Follow follow) {
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getFollowerId, follow.getFollowerId())
                .eq(Follow::getFollowingId, follow.getFollowingId());
        Follow follow1 = baseMapper.selectOne(wrapper);
        return follow1 != null;

    }

    @Override
    public boolean deleteFollow(Integer id, Integer followingId) {
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getFollowerId, id)
                .eq(Follow::getFollowingId, followingId);
        int delete = baseMapper.delete(wrapper);
        return delete > 0;
    }
}
