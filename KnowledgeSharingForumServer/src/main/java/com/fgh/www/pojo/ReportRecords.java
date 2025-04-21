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
 * 举报记录表
 * </p>
 *
 * @author fgh
 * @since 2025-02-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("report_records")
public class ReportRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 举报人id
     */
    private Integer reporterUserId;

    /**
     * 举报原因
     */
    private String reportReason;

    /**
     * 举报类型（0：文章，1：评论）
     */
    private Integer reportedType;

    /**
     * 被举报文章的id
     */
    private Integer reportedArticleId;

    /**
     * 被举报评论的id
     */
    private Integer reportedCommentId;

    /**
     * 举报的时间
     */
    private String reportedTime;

    /**
     * 审核状态（待审核，审核通过，审核不通过）
     */
    private String auditStatus;

    /**
     * 审核通过/驳回的详细说明
     */
    private String auditComment;

    /**
     * 审核时间
     */
    private String auditTime;

    /**
     * 审核人员id
     */
    private Integer auditorUserId;

    /**
     * deleted逻辑删除1为删除
     */
    private Integer deleted;


}
