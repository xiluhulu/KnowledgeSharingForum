package com.fgh.www.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.fgh.www.common.util.Jwt_Utils;
import com.fgh.www.pojo.Likes;
import com.fgh.www.mapper.LikesMapper;
import com.fgh.www.service.ILikesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 点赞 服务实现类
 * </p>
 *
 * @author fgh
 * @since 2024-12-19
 */
@Service
public class LikesServiceImpl extends ServiceImpl<LikesMapper, Likes> implements ILikesService {

    @Transactional
    @Override
    public void set(Likes likes, String token) {
        Integer id = Jwt_Utils.getId(token);
        LambdaQueryWrapper<Likes> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Likes::getFid, likes.getFid()).eq(Likes::getUserId, id);
        Likes likes1 = baseMapper.selectOne(wrapper);
        if (likes1 != null) {
            baseMapper.deleteById(likes1);
        } else {
            likes.setUserId(id);
            baseMapper.insert(likes);
        }
    }
    //查询是否点赞
    @Override
    public boolean selectOne(Likes likes) {
        LambdaQueryWrapper<Likes> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Likes::getFid, likes.getFid())
                        .eq(Likes::getUserId, likes.getUserId());
        Likes likes1 = baseMapper.selectOne(wrapper);
        return likes1!=null;
    }
    //查询点赞数量
    @Override
    public Long getArticleLikeCount(Integer fid) {
        LambdaQueryWrapper<Likes> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Likes::getFid, fid);
        return baseMapper.selectCount(wrapper);
    }
//    删除点赞
    @Override
    public boolean deleteLike(Integer fid, Integer id) {
        LambdaQueryWrapper<Likes> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Likes::getFid, fid)
                        .eq(Likes::getUserId, id);
        int delete = baseMapper.delete(wrapper);
        return delete > 0;
    }
}
