package com.fgh.www.vo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 博客信息
 * </p>
 *
 * @author fgh
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ArticleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */

    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 简介
     */
    private String descr;

    /**
     * 封面
     */
    private String cover;

    /**
     * 标签
     */
    private String tags;

    /**
     * 发布人
     */
    private String userName;
    /**
     * 头像
     */
    private String avatar;

    /**
     * 发布日期
     */
    private String date;

    /**
     * 浏览量
     */
    private Integer readCount;

    /**
     * 分类
     */
    private String categoryName;
    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 是否被当前用户点赞
     */
    private Boolean userLike;

    /**
     * 是否被当前用户收藏
     */
    private Boolean userCollect;
    /**
     * 文章被收藏的数量
     */
    private Integer collectCount;
    /**
     * 文章被点赞的数量
     */
    private Integer likeCount;
    /**
     * 该作者的文章总量
     */
    private Long articleCount;

    private Integer UserId;
    private Boolean isLike;
    private String replyUser;  // 回复给谁 就是谁的名称

}
