package com.fgh.www.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.mapper.ArticleMapper;
import com.fgh.www.mapper.CommentMapper;
import com.fgh.www.pojo.Article;
import com.fgh.www.pojo.Comment;
import com.fgh.www.pojo.ReportRecords;
import com.fgh.www.mapper.ReportRecordsMapper;
import com.fgh.www.pojo.User;
import com.fgh.www.service.IArticleService;
import com.fgh.www.service.ICommentService;
import com.fgh.www.service.IReportRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fgh.www.vo.user.MyReportVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 举报记录表 服务实现类
 * </p>
 *
 * @author fgh
 * @since 2025-02-20
 */
@Service
public class ReportRecordsServiceImpl extends ServiceImpl<ReportRecordsMapper, ReportRecords> implements IReportRecordsService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private IArticleService articleService;
    @Autowired
    private ICommentService commentService;

    //查看是否存在举报
    @Override
    public boolean selectOne(ReportRecords reportRecords) {
        LambdaQueryWrapper<ReportRecords> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReportRecords::getReporterUserId, reportRecords.getReporterUserId());
        wrapper.eq(ReportRecords::getAuditStatus, reportRecords.getAuditStatus());
        if (reportRecords.getReportedType() == 0) {
            wrapper.eq(ReportRecords::getReportedArticleId, reportRecords.getReportedArticleId());
        }
        if (reportRecords.getReportedType() == 1) {
            wrapper.eq(ReportRecords::getReportedCommentId, reportRecords.getReportedCommentId());
        }
        ReportRecords one = baseMapper.selectOne(wrapper);
        return one != null;
    }

    //分页查询举报记录
    @Transactional
    @Override
    public Page<MyReportVO> selectPage(ReportRecords reportRecords, Integer pageNum, Integer pageSize) {
        MPJLambdaWrapper<ReportRecords> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(ReportRecords.class)
                .selectAs(ReportRecords::getReportedArticleId, MyReportVO::getArticleId)
                .selectAs(ReportRecords::getReportedCommentId, MyReportVO::getCommentId);

        wrapper.leftJoin(User.class, "t1", User::getId, ReportRecords::getAuditorUserId)
                .selectAs("t1.name", MyReportVO::getAuditorUserName);
        wrapper.leftJoin(User.class, "t2", User::getId, ReportRecords::getReporterUserId)
                .selectAs("t2.name", MyReportVO::getReporterUserName);
        wrapper.eq(ReportRecords::getReporterUserId, reportRecords.getReporterUserId()).orderByDesc(ReportRecords::getId);
        Page<MyReportVO> page = baseMapper.selectJoinPage(new Page<>(pageNum, pageSize), MyReportVO.class, wrapper);
        page.getRecords().forEach(item -> {
            if (item.getReportedType() == 0) {
                LambdaQueryWrapper<Article> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.eq(Article::getId, item.getArticleId());
                Article article = articleMapper.selectOne(wrapper1);
                item.setTitleOrCommentContent(article.getTitle());
            }
            if (item.getReportedType() == 1) {
                LambdaQueryWrapper<Comment> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.eq(Comment::getId, item.getCommentId());
                Comment comment = commentMapper.selectOne(wrapper1);
                item.setTitleOrCommentContent(comment.getContent());
            }
            item.setAuditStatusBoolean(!"待审核".equals(item.getAuditStatus()));
        });
        return page;
    }

    //分页查询待审核的举报记录
    @Override
    public Page<MyReportVO> selectUnreviewedPage(ReportRecords reportRecords, Integer pageNum, Integer pageSize) {
        MPJLambdaWrapper<ReportRecords> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(ReportRecords.class)
                .selectAs(ReportRecords::getReportedArticleId, MyReportVO::getArticleId)
                .selectAs(ReportRecords::getReportedCommentId, MyReportVO::getCommentId);
        wrapper.leftJoin(User.class, "t1", User::getId, ReportRecords::getAuditorUserId)
                .selectAs("t1.name", MyReportVO::getAuditorUserName);
        wrapper.leftJoin(User.class, "t2", User::getId, ReportRecords::getReporterUserId)
                .selectAs("t2.name", MyReportVO::getReporterUserName);


        wrapper.eq(ReportRecords::getAuditStatus, "待审核").orderByDesc(ReportRecords::getId);
        Page<MyReportVO> page = baseMapper.selectJoinPage(new Page<>(pageNum, pageSize), MyReportVO.class, wrapper);
        page.getRecords().forEach(item -> {
            if (item.getReportedType() == 0) {
                LambdaQueryWrapper<Article> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.eq(Article::getId, item.getArticleId());
                Article article = articleMapper.selectOne(wrapper1);
                item.setTitleOrCommentContent(article.getTitle());
            }
            if (item.getReportedType() == 1) {
                LambdaQueryWrapper<Comment> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.eq(Comment::getId, item.getCommentId());
                Comment comment = commentMapper.selectOne(wrapper1);
                System.out.println("--------------------------------------------" + comment);
                item.setTitleOrCommentContent(comment.getContent());
            }
            item.setAuditStatusBoolean(!"待审核".equals(item.getAuditStatus()));
        });
        return page;
    }

    //举报记录分页查询
    @Override
    public Page<MyReportVO> selectPageAll(ReportRecords reportRecords, Integer pageNum, Integer pageSize) {
        MPJLambdaWrapper<ReportRecords> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(ReportRecords.class)
                .selectAs(ReportRecords::getReportedArticleId, MyReportVO::getArticleId)
                .selectAs(ReportRecords::getReportedCommentId, MyReportVO::getCommentId);
        wrapper.leftJoin(User.class, "t1", User::getId, ReportRecords::getAuditorUserId)
                .selectAs("t1.name", MyReportVO::getAuditorUserName);
        wrapper.leftJoin(User.class, "t2", User::getId, ReportRecords::getReporterUserId)
                .selectAs("t2.name", MyReportVO::getReporterUserName);
        wrapper.orderByDesc(ReportRecords::getId);
        Page<MyReportVO> page = baseMapper.selectJoinPage(new Page<>(pageNum, pageSize), MyReportVO.class, wrapper);
        page.getRecords().forEach(item -> {
            if (item.getReportedType() == 0) {
                Article article = articleMapper.selectById(item.getArticleId());
                item.setTitleOrCommentContent(article.getTitle());
            }
            if (item.getReportedType() == 1) {
                Comment comment = commentMapper.selectById(item.getCommentId());
                item.setTitleOrCommentContent(comment.getContent());
            }
            item.setAuditStatusBoolean(!"待审核".equals(item.getAuditStatus()));
        });
        return page;
    }

    @Transactional
    @Override
    public boolean updatesubmitAuditResult(ReportRecords reportRecords) {
        if (reportRecords.getReportedType() == 0) {
            boolean b = articleService.bannedById(reportRecords.getReportedArticleId());
            baseMapper.updateById(reportRecords);
            LambdaUpdateWrapper<ReportRecords> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(ReportRecords::getReportedArticleId,reportRecords.getReportedArticleId())
                    .eq(ReportRecords::getAuditStatus,"待审核")
                    .set(ReportRecords::getAuditComment,"其他用户举报审核通过")
                    .set(ReportRecords::getAuditorUserId,reportRecords.getAuditorUserId())
                    .set(ReportRecords::getAuditTime,reportRecords.getAuditTime())
                    .set(ReportRecords::getAuditStatus,"审核通过");
            baseMapper.update(wrapper);
            return b;
        }
        if (reportRecords.getReportedType() == 1) {
            boolean b = commentService.bannedById(reportRecords.getReportedCommentId());
            baseMapper.updateById(reportRecords);
            LambdaUpdateWrapper<ReportRecords> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(ReportRecords::getReportedCommentId,reportRecords.getReportedCommentId())
                    .eq(ReportRecords::getAuditStatus,"待审核")
                    .set(ReportRecords::getAuditorUserId,reportRecords.getAuditorUserId())
                    .set(ReportRecords::getAuditTime,reportRecords.getAuditTime())
                    .set(ReportRecords::getAuditComment,"其他用户举报审核通过")
                    .set(ReportRecords::getAuditStatus,"审核通过");
            baseMapper.update(wrapper);
            return b;
        }
        return false;
    }
}
