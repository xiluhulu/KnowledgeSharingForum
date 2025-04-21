package com.fgh.www.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 发布人ID
     */
    private Integer userId;

    /**
     * 发布日期
     */
    private String date;

    /**
     * 浏览量
     */
    private Integer readCount;
    /**
     * 文章被点赞的数量
     */
    private Integer likeCount;
    /**
     * 分类ID
     */
    private Integer categoryId;

//    deleted逻辑删除1为删除

    private Integer deleted;
//封禁，0为开启，1为封禁
    private Integer banned;

}
