package com.fgh.www.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.fgh.www.common.util.Jwt_Utils;
import com.fgh.www.mapper.UserMapper;
import com.fgh.www.pojo.Comment;
import com.fgh.www.mapper.CommentMapper;
import com.fgh.www.pojo.CommentVO;
import com.fgh.www.pojo.FrontCommentVO;
import com.fgh.www.pojo.User;
import com.fgh.www.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fgh.www.vo.user.MyCommentVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 * .
 *
 * @author fgh
 * @since 2024-12-19
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Page<CommentVO> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
        MPJLambdaWrapper<Comment> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(Comment.class)
                .eq(Comment::getDeleted,0)
                .eq(Comment::getBanned,0)
                .leftJoin(User.class, User::getId, Comment::getUserId)
                .selectAs(User::getName, CommentVO::getUserName)
                .orderByDesc(Comment::getId);
        if(StringUtils.isNoneBlank(comment.getContent())){
            wrapper.like(Comment::getContent, comment.getContent());
        }
        return baseMapper.selectJoinPage(new Page<>(pageNum, pageSize), CommentVO.class, wrapper);
    }

    /**
     * 获取评论列表（包括回复的评论）
     * pid为父级节点
     * root_id为根节点
     * fid文章关联id
     * 逻辑：
     *      当pid为空且rootId==评论id时为一级评论
     *      当rootId！=评论ID时为二级评论，且pid与rootid一致
     *      当pid！=rootid时为回复别人的二级评论，本身也是一个二级评论
     * @param comment
     * @return
     */

    @Override
    @Transactional
    public List<FrontCommentVO> selectForUser(Comment comment) {

        //获取一级评论
        MPJLambdaWrapper<Comment> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(Comment.class)
                .eq(Comment::getDeleted,0)
                .eq(Comment::getBanned,0)
                .leftJoin(User.class, User::getId, Comment::getUserId)
                .selectAs(User::getName, FrontCommentVO::getUserName)
                .selectAs(User::getAvatar, FrontCommentVO::getAvatar)
                .leftJoin(User.class, User::getId, Comment::getUserId)
                .eq(Comment::getFid, comment.getFid())
                .isNull(Comment::getPid)
                .orderByDesc(Comment::getId);
        List<FrontCommentVO> commentVOS = baseMapper.selectJoinList(FrontCommentVO.class, wrapper);

//        获取二级评论
        commentVOS.forEach(item -> {
            MPJLambdaWrapper<Comment> wrapper2 = new MPJLambdaWrapper<>();
            wrapper2.selectAll(Comment.class)
                    .eq(Comment::getDeleted,0)
                    .eq(Comment::getBanned,0)
                    .leftJoin(User.class, User::getId, Comment::getUserId)
                    .selectAs(User::getName, FrontCommentVO::getUserName)
                    .selectAs(User::getAvatar, FrontCommentVO::getAvatar)
                    .eq(Comment::getRootId, item.getId())
                    .orderByDesc(Comment::getId);
            List<FrontCommentVO> commentVOS1 = baseMapper.selectJoinList(FrontCommentVO.class, wrapper2);

            commentVOS1.forEach(item1 -> {
                if (item1.getPid() != null) {
//                    设置评论被回复的名称和被回复的id
                    MPJLambdaWrapper<Comment> wrapper1 = new MPJLambdaWrapper<>();
                    wrapper1.selectAll(Comment.class)
                            .eq(Comment::getDeleted,0)
                            .eq(Comment::getBanned,0)
                            .leftJoin(User.class, User::getId, Comment::getUserId)
                            .selectAs(User::getName, FrontCommentVO::getReplyUser)
                            .selectAs(User::getId,FrontCommentVO::getReplyUserId)
                            .eq(Comment::getId, item1.getPid());
                    FrontCommentVO frontCommentVO = baseMapper.selectJoinOne(FrontCommentVO.class, wrapper1);
                    if(ObjectUtils.isNotEmpty(frontCommentVO)){
                        System.out.println("frontCommentVO = " + frontCommentVO);
                        item1.setReplyUser(frontCommentVO.getReplyUser());
                        item1.setReplyUserId(frontCommentVO.getReplyUserId());
                    }
                }
            });
            if (!commentVOS1.isEmpty()) {
                commentVOS1.remove(commentVOS1.size()-1);
            }
            item.setChildren(commentVOS1);
        });

        return commentVOS;
    }
@Transactional
    @Override
    public Long selectCount(Integer fid) {
        //先获存在的一级评论
        Long l= 0L;
        LambdaQueryWrapper<Comment> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Comment::getFid, fid)
                .eq(Comment::getDeleted, 0)
                .eq(Comment::getBanned, 0)
                .isNull(Comment::getPid);
        List<Comment> comments = baseMapper.selectList(wrapper1);
        l+= (long) comments.size();
        for (Comment comment : comments) {
            LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
            wrapper.isNotNull(Comment::getPid).eq(Comment::getRootId, comment.getId()).eq(Comment::getDeleted, 0).eq(Comment::getBanned, 0);
            List<Comment> comments1 = baseMapper.selectList(wrapper);
            l += (long) comments1.size();
        }

        return l;
    }

    @Transactional
    @Override
    public void add(Comment comment, String token) {
        System.out.println("comment = " + comment);
        Integer id = Jwt_Utils.getId(token);
        comment.setUserId(id);
        comment.setTime(DateUtil.today());
        baseMapper.insert(comment);

        if (comment.getRootId() == null) {
            comment.setRootId(comment.getId());
            baseMapper.updateById(comment);
        }

    }

    //分页查询我的评论
    @Override
    public Page<MyCommentVO> selectUserPage(Comment comment, Integer pageNum, Integer pageSize, String token) {
        Integer id = Jwt_Utils.getId(token);
        MPJLambdaWrapper<Comment> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(Comment.class)
                .eq(Comment::getUserId, id)
                .eq(Comment::getDeleted, 0)
                .eq(Comment::getBanned, 0)
                .selectAs(Comment::getId, MyCommentVO::getId)//评论id
                .selectAs(Comment::getContent, MyCommentVO::getContent)//评论内容
                .selectAs(Comment::getTime, MyCommentVO::getTime)//评论时间
                .selectAs(Comment::getFid, MyCommentVO::getFid)//评论文章id
                .leftJoin(User.class, User::getId, Comment::getUserId)
                .selectAs(User::getAvatar, MyCommentVO::getAvatar)//用户头像
                .selectAs(User::getName, MyCommentVO::getName);//用户名


        Page<MyCommentVO> page = baseMapper.selectJoinPage(new Page<>(pageNum, pageSize), MyCommentVO.class, wrapper);
        return page;
    }
//    根据id查询评论
    @Override
    public CommentVO selectGetById(Integer id) {
        MPJLambdaWrapper<Comment> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(Comment.class)
                .eq(Comment::getDeleted, 0)
                .eq(Comment::getBanned, 0)
                .leftJoin(User.class, User::getId, Comment::getUserId)
                .selectAs(User::getName, CommentVO::getUserName)
                .eq(Comment::getId, id)

                .orderByDesc(Comment::getId);
        CommentVO commentVO = baseMapper.selectJoinOne(CommentVO.class, wrapper);
        return commentVO;
    }
//    批量逻辑删除
    @Override
    public boolean logicRemoveBatchByIds(List<Integer> ids) {
        if(ids==null||ids.isEmpty()){
            return false;
        }
        LambdaUpdateWrapper<Comment> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Comment::getId,ids).set(Comment::getDeleted,1);
        int update = baseMapper.update(wrapper);
        return update > 0;
    }
//    逻辑删除
    @Override
    public boolean logicRemoveById(Integer id) {
        LambdaUpdateWrapper<Comment> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Comment::getId,id).set(Comment::getDeleted,1);
        int update = baseMapper.update(wrapper);
        return update > 0;
    }
//    封禁
    @Override
    public boolean bannedById(Integer reportedCommentId) {
        LambdaUpdateWrapper<Comment> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Comment::getId,reportedCommentId).set(Comment::getBanned,1);
        int update = baseMapper.update(wrapper);
        return update > 0;
    }
}
