package com.fgh.www.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.pojo.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fgh.www.pojo.CommentVO;
import com.fgh.www.pojo.FrontCommentVO;
import com.fgh.www.vo.user.MyCommentVO;

import java.util.List;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author fgh
 * @since 2024-12-19
 */
public interface ICommentService extends IService<Comment> {
//    分页查询评论
    Page<CommentVO> selectPage(Comment comment, Integer pageNum, Integer pageSize);
    //通过fid查询评论数据
    List<FrontCommentVO> selectForUser(Comment comment);
    //查询评论总数
    Long selectCount(Integer fid);
    //添加评论
    void add(Comment comment, String token);
//    分页查询我的评论
    Page<MyCommentVO> selectUserPage(Comment comment, Integer pageNum, Integer pageSize, String token);
//    通过id查询评论
    CommentVO selectGetById(Integer id);
//    批量逻辑删除
    boolean logicRemoveBatchByIds(List<Integer> ids);
//    逻辑删除
    boolean logicRemoveById(Integer id);
//    封禁评论
    boolean bannedById(Integer reportedCommentId);
}
