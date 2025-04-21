package com.fgh.www.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author fgh
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 内容
     */
    private String content;

    /**
     * 评论人
     */
    private Integer userId;

    /**
     * 父级ID
     */
    private Integer pid;

    /**
     * 根节点ID
     */
    private Integer rootId;

    /**
     * 评论时间
     */
    private String time;

    /**
     * 关联ID
     */
    private Integer fid;



    //    deleted逻辑删除1为删除
    private Integer deleted;
    //封禁，0为开启，1为封禁
    private Integer banned;

    @TableField(exist = false)
    private List<Comment> children;
}
