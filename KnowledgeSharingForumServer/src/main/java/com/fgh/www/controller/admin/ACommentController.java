package com.fgh.www.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.common.util.R;
import com.fgh.www.pojo.Comment;
import com.fgh.www.pojo.CommentVO;
import com.fgh.www.pojo.FrontCommentVO;
import com.fgh.www.service.ICommentService;
import com.fgh.www.vo.user.ArticleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 评论
 * </p>
 *
 * @author fgh
 * @since 2024-12-19
 */
@RestController
@RequestMapping("/admin-api/comment")
public class ACommentController {
    @Autowired
    private ICommentService commentService;
    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public R selectPage(Comment comment,
                        @RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum,
                        @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize) {
        Page<CommentVO> page = commentService.selectPage(comment, pageNum, pageSize);
        return R.ok("成功").put("data", page);
    }
    /**
     * 批量删除
     */
    @DeleteMapping("/deleteBatch")
    public R deleteBatch(@RequestBody List<Integer> ids) {
        boolean b = commentService.logicRemoveBatchByIds(ids);
        if (!b){
            return R.error("删除失败");
        }
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public R deleteById(@PathVariable(name="id") Integer id) {
        boolean b = commentService.logicRemoveById(id);
        if (!b){
            return R.error("删除失败");
        }
        return R.ok("删除成功");
    }
    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public R selectById(@PathVariable(name = "id") Integer id) {
        CommentVO commentVO = commentService.selectGetById(id);
        return R.ok().put("data", commentVO);
    }
////    查询评论以及回复
//    @GetMapping("/selectForUser")
//    public R selectForUser(Comment comment) {
//        List<FrontCommentVO> list = commentService.selectForUser(comment);
//        return R.ok().put("data",list);
//    }
//    //查询评论总数
//    @GetMapping("/selectCount")
//    public R selectCount(@RequestParam("fid") Integer fid) {
//        Long count = commentService.selectCount(fid);
//        return R.ok().put("data",count);
//    }
//
//    /**
//     * 新增
//     */
//    @PostMapping("/add")
//    public R add(@RequestBody Comment comment,@RequestHeader("token") String token) {
//
//        commentService.add(comment,token);
//        return R.ok();
//    }
}
