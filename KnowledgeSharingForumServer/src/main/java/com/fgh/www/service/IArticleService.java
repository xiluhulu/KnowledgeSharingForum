package com.fgh.www.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.pojo.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fgh.www.vo.user.ArticleDetailAuthorData;
import com.fgh.www.vo.user.ArticleVO;

import java.util.List;

/**
 * <p>
 * 博客信息 服务类
 * </p>
 *
 * @author fgh
 * @since 2024-12-19
 */
public interface IArticleService extends IService<Article> {

//    客户端查询文章列表
    Page<ArticleVO> userSelectPage(Article article, Integer pageNum, Integer pageSize,String categoryName);
    //分页查询
    Page<ArticleVO> selectPage(Article article, Integer pageNum, Integer pageSize,String categoryName);
    //    根据id查询文章详情
    ArticleVO selectGetById(Integer id,String token);

    boolean addReadCount(Integer articleId);
//分页查询当前用户的文章列表
    Page<ArticleVO> selectUserPage(Article article, Integer pageNum, Integer pageSize, String token);


    List<ArticleVO> selectHourRank();

//    新添加文章
    boolean addArticle(Article article);
    //    获取文章详情作者数据
    ArticleDetailAuthorData getArticleDetailAuthorData(Integer authorId);


    //分页查询我的文章
    Page<ArticleVO> getMyArticlePage(Integer id, Integer pageNum, Integer pageSize);
//逻辑删除
    boolean logicRemoveById(Integer id);
//逻辑批量删除
    boolean logicRemoveBatchByIds(List<Integer> ids);
//封禁文章
    boolean bannedById(Integer reportedArticleId);
}
