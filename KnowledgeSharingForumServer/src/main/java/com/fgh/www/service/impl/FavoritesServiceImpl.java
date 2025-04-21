package com.fgh.www.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.pojo.Article;
import com.fgh.www.pojo.Favorites;
import com.fgh.www.mapper.FavoritesMapper;
import com.fgh.www.pojo.User;
import com.fgh.www.service.IFavoritesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fgh.www.vo.user.ArticleVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 收藏表 服务实现类
 * </p>
 *
 * @author fgh
 * @since 2025-02-20
 */
@Service
public class FavoritesServiceImpl extends ServiceImpl<FavoritesMapper, Favorites> implements IFavoritesService {
    //查询是否收藏
    @Override
    public boolean selectOne(Favorites favorites) {
        LambdaQueryWrapper<Favorites> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorites::getArticleId, favorites.getArticleId())
                .eq(Favorites::getUserId, favorites.getUserId());

        Favorites favorites1 = baseMapper.selectOne(wrapper);
        return favorites1 != null;

    }
//删除收藏
    @Override
    public boolean deleteFavorite(Integer articleId, Integer id) {
        LambdaQueryWrapper<Favorites> wrapper = new LambdaQueryWrapper<Favorites>();
                wrapper.eq(Favorites::getArticleId, articleId)
                .eq(Favorites::getUserId, id);
        int delete = baseMapper.delete(wrapper);
        return delete > 0;
    }
//查询收藏数量
    @Override
    public Long getArticleFavoriteCount(Integer articleId) {
        LambdaQueryWrapper<Favorites> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorites::getArticleId, articleId);

        return  baseMapper.selectCount(wrapper);
    }
//
    @Override
    public Page<ArticleVO> selectPage(Integer pageNum, Integer pageSize, Integer id) {
        MPJLambdaWrapper<Favorites> wrapper = new MPJLambdaWrapper<>();
        wrapper//收藏表
                .eq(Favorites::getUserId, id)//用户id
                .selectAs(Favorites::getArticleId, ArticleVO::getId)//文章id
                .leftJoin(Article.class, Article::getId, Favorites::getArticleId)//文章表
                .selectAs(Article::getTitle, ArticleVO::getTitle)//文章标题
                .selectAs(Article::getContent, ArticleVO::getContent)//文章内容
                .selectAs(Article::getDescr, ArticleVO::getDescr)//文章描述
                .selectAs(Article::getCover, ArticleVO::getCover)//文章封面
                .selectAs(Article::getTags, ArticleVO::getTags)//文章标签
                .selectAs(Article::getDate, ArticleVO::getDate)//文章时间
                .selectAs(Article::getReadCount,ArticleVO::getReadCount)
                .leftJoin(User.class, User::getId, Article::getUserId)
                .selectAs(User::getName, ArticleVO::getUserName)
                .orderByDesc(Favorites::getId);
        Page<ArticleVO> page = baseMapper.selectJoinPage(new Page<>(pageNum, pageSize), ArticleVO.class, wrapper);
        return page;
    }
}
