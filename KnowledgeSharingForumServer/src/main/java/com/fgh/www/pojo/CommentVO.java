package com.fgh.www.pojo;

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
public class CommentVO implements Serializable {

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
     * 评论人id
     */
    private String userId;
    /**
     * 评论人
     */
    private String userName;

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


}
