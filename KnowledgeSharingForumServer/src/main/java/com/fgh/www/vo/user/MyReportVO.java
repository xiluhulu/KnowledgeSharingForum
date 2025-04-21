package com.fgh.www.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyReportVO {
    //id
    private Integer id;
    //文章id
    private Integer articleId;
    //评论id
    private Integer commentId;
    //举报人名称
    private String reporterUserName;
    //举报原因
    private String reportReason;
    //举报类型（0：文章，1：评论）
    private Integer reportedType;
    //举报时间
    private String reportedTime;
    //审核状态（待审核，审核通过，审核不通过）
    private String auditStatus;
    //审核状态（false[待审核],true[审核通过，审核不通过]）
    private Boolean auditStatusBoolean;
    //被举报的文章标题或评论内容
    private String titleOrCommentContent;
    //审核说明
    private String auditComment;
    //审核时间
    private String auditTime;
    //审核人员名称
    private String auditorUserName;
}
