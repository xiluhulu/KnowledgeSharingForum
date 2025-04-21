package com.fgh.www.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrontCommentVO {
    //评论id
    private int id;
    //评论内容
    private String content;
    //用户id
    private int userId;
    //父id
    private String pid;
    //根id
    private int rootId;
    //评论时间
    private String time;
    //文章id
    private int fid;
    //用户名
    private String userName;
    //头像
    private String avatar;
    //被回复的用户id
    private int replyUserId;
    //被回复的用户名
    private String replyUser;
    //子评论
    private List<FrontCommentVO> children;
}
