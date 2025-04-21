package com.fgh.www.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.fgh.www.common.util.Jwt_Utils;
import com.fgh.www.mapper.FollowMapper;
import com.fgh.www.mapper.LikesMapper;
import com.fgh.www.mapper.UserMapper;
import com.fgh.www.pojo.*;
import com.fgh.www.mapper.ArticleMapper;
import com.fgh.www.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fgh.www.vo.user.ArticleDetailAuthorData;
import com.fgh.www.vo.user.ArticleVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 博客信息 服务实现类
 * </p>
 *
 * @author fgh
 * @since 2024-12-19
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
    @Autowired
    private LikesMapper likesMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private FollowMapper followMapper;
    private static final String ARTICLE_CLICK_KEY = "article:click:";

    /**
     * 客户端文章分页
     *
     * @param article
     * @param pageNum
     * @param pageSize
     * @param categoryName
     * @return
     */
    @Override
    public Page<ArticleVO> userSelectPage(Article article, Integer pageNum, Integer pageSize, String categoryName) {
        MPJLambdaWrapper<Article> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(Article.class)
                .eq(Article::getDeleted, 0)
                .eq(Article::getBanned, 0)
                .leftJoin(Category.class, Category::getId, Article::getCategoryId)
                .selectAs(Category::getName, ArticleVO::getCategoryName)
                .leftJoin(User.class, User::getId, Article::getUserId)
                .selectAs(User::getName, ArticleVO::getUserName)
                .orderByDesc(Article::getId);
        if (StringUtils.isNoneBlank(categoryName)) {
            wrapper.eq(Category::getName, categoryName);
        }
        if (StringUtils.isNoneBlank(article.getTitle())) {
            wrapper.like(Article::getTitle, article.getTitle());
        }
        Page<ArticleVO> page = baseMapper.selectJoinPage(new Page<>(pageNum, pageSize), ArticleVO.class, wrapper);
        return page;
    }

    @Override
    public Page<ArticleVO> selectPage(Article article, Integer pageNum, Integer pageSize, String categoryName) {

        MPJLambdaWrapper<Article> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(Article.class)
                .eq(Article::getDeleted, 0)
                .eq(Article::getBanned, 0)
                .leftJoin(Category.class, Category::getId, Article::getCategoryId)
                .selectAs(Category::getName, ArticleVO::getCategoryName)
                .leftJoin(User.class, User::getId, Article::getUserId)
                .selectAs(User::getName, ArticleVO::getUserName).orderByDesc(Article::getId);
        if (StringUtils.isNoneBlank(categoryName)) {
            wrapper.eq(Category::getName, categoryName);
        }
        if (StringUtils.isNoneBlank(article.getTitle())) {
            wrapper.like(Article::getTitle, article.getTitle());
        }


        Page<ArticleVO> page = baseMapper.selectJoinPage(new Page<>(pageNum, pageSize), ArticleVO.class, wrapper);
        return page;
    }

    @Override
    public ArticleVO selectGetById(Integer id, String token) {
        MPJLambdaWrapper<Article> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(Article.class)
                .leftJoin(User.class, User::getId, Article::getUserId)
                .selectAs(User::getId, ArticleVO::getUserId)
                .selectAs(User::getName, ArticleVO::getUserName)
                .selectAs(User::getAvatar, ArticleVO::getAvatar)
                .selectAs(User::getName, ArticleVO::getUserName)
                .eq(Article::getId, id)
                .eq(Article::getDeleted, 0)
                .eq(Article::getBanned, 0);
        ArticleVO articleVO = baseMapper.selectJoinOne(ArticleVO.class, wrapper);
        if (articleVO == null) {
            return null;
        }
        //设置文章总量
        QueryWrapper<Article> wrapper1 = new QueryWrapper<>();
        Long l = baseMapper.selectCount(wrapper1);
        articleVO.setArticleCount(l);
        //文章点赞总量
        LambdaQueryWrapper<Likes> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Likes::getFid, id).eq(Likes::getUserId, articleVO.getUserId());
        Long l1 = likesMapper.selectCount(wrapper2);
        articleVO.setLikeCount(1);

//        查看当前登录用户是否点赞
        if (StringUtils.isNoneBlank(token)) {
            Integer id1 = Jwt_Utils.getId(token);
            LambdaQueryWrapper<Likes> wrapper3 = new LambdaQueryWrapper<>();
            wrapper3.eq(Likes::getFid, id).eq(Likes::getUserId, id1);
            Likes likes = likesMapper.selectOne(wrapper3);
            articleVO.setIsLike(likes != null);
        }
//        记录浏览量
//        先获取原来浏览量
        Article article = baseMapper.selectById(id);
        article.setReadCount(article.getReadCount() + 1);
        articleVO.setReadCount(article.getReadCount() );
        baseMapper.updateById(article);

        // 记录点击
        ArticleClick articleClick = new ArticleClick(articleVO.getId(), new Date().getTime());
        String jsonStr = JSONUtil.toJsonStr(articleClick);
        redisTemplate.opsForList().rightPush(ARTICLE_CLICK_KEY, jsonStr);

        return articleVO;

    }


    @Override
    public boolean addReadCount(Integer articleId) {
        Article article = baseMapper.selectById(articleId);
        LambdaUpdateWrapper<Article> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Article::getId, articleId).set(Article::getReadCount, article.getReadCount() + 1);
        int i = baseMapper.update(wrapper);
        return i > 0;
    }

    //分页查询当前用户的文章列表
    @Override
    public Page<ArticleVO> selectUserPage(Article article, Integer pageNum, Integer pageSize, String token) {
        Integer id = Jwt_Utils.getId(token);

        MPJLambdaWrapper<Article> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(Article.class)
                .leftJoin(Category.class, Category::getId, Article::getCategoryId)
                .selectAs(Category::getName, ArticleVO::getCategoryName)
                .leftJoin(User.class, User::getId, Article::getUserId)
                .selectAs(User::getName, ArticleVO::getUserName)
                .eq(Article::getUserId, id)
                .eq(Article::getDeleted, 0)
                .eq(Article::getBanned, 0);


        Page<ArticleVO> page = baseMapper.selectJoinPage(new Page<>(pageNum, pageSize), ArticleVO.class, wrapper);
        return page;
    }

    @Override
    public List<ArticleVO> selectHourRank() {
        // 1. 定义 Redis 中的列表键
        String hotArticlesKey = "hot_articles";

        // 2. 从 Redis 中获取存储的文章 ID
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        List<Object> articleIdList = listOps.range(hotArticlesKey, 0, -1);

        // 3. 检查是否获取到 ID
        if (articleIdList == null || articleIdList.isEmpty()) {
            // 返回空列表
            return null;
        }
        System.out.println(articleIdList);
        ArrayList<ArticleVO> list = new ArrayList<>();
        // 4. 查询对应的文章
        articleIdList.forEach(articleId -> {
            MPJLambdaWrapper<Article> wrapper = new MPJLambdaWrapper<>();
            wrapper.selectAll(Article.class)
                    .eq(Article::getId, articleId)
                    .leftJoin(Category.class, Category::getId, Article::getCategoryId)
                    .selectAs(Category::getName, ArticleVO::getCategoryName)
                    .leftJoin(User.class, User::getId, Article::getUserId)
                    .selectAs(User::getName, ArticleVO::getUserName);
            ArticleVO articleVO = baseMapper.selectJoinOne(ArticleVO.class, wrapper);
            list.add(articleVO);
        });

        // 5. 返回结果列表
        return list;


    }

    /**
     * 添加新文章
     *
     * @param article
     * @return 已重置
     */
    @Override
    public boolean addArticle(Article article) {
        //  获取id
        //控制器中获取用户身份信息
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        //获取用户名
        String username = (String) authentication.getPrincipal();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);
        article.setUserId(user.getId());
        article.setDate(DateUtil.today());
        article.setBanned(0);
        int insert = baseMapper.insert(article);
        return insert > 0;
    }

    /**
     * 获取文章作者信息
     *
     * @param authorId
     */

    @Transactional
    @Override
    public ArticleDetailAuthorData getArticleDetailAuthorData(Integer authorId) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Article::getUserId, authorId).eq(Article::getDeleted, 0).eq(Article::getBanned, 0);

        Long articlesReadNumber = 0L;
        List<Article> articles = baseMapper.selectList(wrapper);
        for (Article article : articles) {
            articlesReadNumber += article.getReadCount();
        }
        Long articlesNumber = (long) articles.size();
        LambdaQueryWrapper<Follow> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Follow::getFollowingId, authorId);
        Long fansNumber = followMapper.selectCount(wrapper1);
        User user = userMapper.selectById(authorId);
        return new ArticleDetailAuthorData(articlesReadNumber, articlesNumber, fansNumber, user.getName(), user.getAvatar());

    }

    /**
     * 客户端业务
     * 分页获取我的文章
     * 需要登录
     *
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Page<ArticleVO> getMyArticlePage(Integer id, Integer pageNum, Integer pageSize) {
        MPJLambdaWrapper<Article> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(Article.class)
                .eq(Article::getDeleted, 0)
                .eq(Article::getBanned, 0)
                .leftJoin(Category.class, Category::getId, Article::getCategoryId)
                .selectAs(Category::getName, ArticleVO::getCategoryName)
                .leftJoin(User.class, User::getId, Article::getUserId)
                .selectAs(User::getName, ArticleVO::getUserName)
                .orderByDesc(Article::getId);
        wrapper.eq(Article::getUserId, id).eq(Article::getDeleted, 0).eq(Article::getBanned, 0);
        Page<ArticleVO> page = baseMapper.selectJoinPage(new Page<>(pageNum, pageSize), ArticleVO.class, wrapper);
        return page;
    }

    //逻辑删除
    @Override
    public boolean logicRemoveById(Integer id) {
        LambdaUpdateWrapper<Article> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Article::getId, id).set(Article::getDeleted, 1);
        int update = baseMapper.update(wrapper);
        return update > 0;
    }

    //逻辑批量删除
    @Override
    public boolean logicRemoveBatchByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        LambdaUpdateWrapper<Article> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Article::getId, ids).set(Article::getDeleted, 1);
        int update = baseMapper.update(wrapper);
        return update > 0;
    }

    @Override
    public boolean bannedById(Integer reportedArticleId) {

        LambdaUpdateWrapper<Article> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Article::getId, reportedArticleId).set(Article::getBanned, 1);
        int update = baseMapper.update(wrapper);

        return update > 0;


    }




}
