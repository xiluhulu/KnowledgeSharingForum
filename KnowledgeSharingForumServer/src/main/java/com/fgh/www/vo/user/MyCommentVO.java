package com.fgh.www.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyCommentVO {
    /**
     * id
     */
    private Integer id;

    /**
     * 评论人id
     */
    private String userId;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 评论人名称
     */
    private String name;
    /**
     * 内容
     */
    private String content;
    /**
     * 评论时间
     */
    private String time;
    /**
     * 关联ID
     */
    private Integer fid;
}
