package com.fgh.www.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.pojo.Favorites;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fgh.www.vo.user.ArticleVO;

/**
 * <p>
 * 收藏表 服务类
 * </p>
 *
 * @author fgh
 * @since 2025-02-20
 */
public interface IFavoritesService extends IService<Favorites> {
    //查询是否收藏
    boolean selectOne(Favorites favorites);
    //删除收藏
    boolean deleteFavorite(Integer articleId, Integer id);
    //查询收藏数量
    Long getArticleFavoriteCount(Integer articleId);
   //分页查询收藏
    Page<ArticleVO> selectPage(Integer pageNum, Integer pageSize, Integer id);
}
