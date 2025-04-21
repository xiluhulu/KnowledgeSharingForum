package com.fgh.www.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 关注表
 * </p>
 *
 * @author fgh
 * @since 2025-02-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("follow")
public class Follow implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关注者的用户ID，外键关联到 User 表的 id
     */
    private Integer followerId;

    /**
     * 被关注者的用户ID，外键关联到 User 表的 id
     */
    private Integer followingId;


}
