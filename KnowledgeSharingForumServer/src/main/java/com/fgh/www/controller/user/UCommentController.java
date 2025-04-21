package com.fgh.www.controller.user;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.common.util.R;
import com.fgh.www.pojo.Comment;
import com.fgh.www.pojo.CommentVO;
import com.fgh.www.pojo.FrontCommentVO;
import com.fgh.www.service.ICommentService;
import com.fgh.www.vo.user.MyCommentVO;
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
@RequestMapping("/user-api/comment")
public class UCommentController {
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
     * 分页查询我的评论
     */
    @GetMapping("/selectUserPage")
    public R selectUserPage(Comment comment,
                            @RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum,
                            @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize,
                            @CookieValue("token") String token) {
        Page<MyCommentVO> page = commentService.selectUserPage(comment, pageNum, pageSize, token);
        return R.ok("成功").put("data", page);
    }
    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
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
        return R.ok();
    }
//    查询评论以及回复
    @GetMapping("/selectForUser")
    public R selectForUser(Comment comment) {
        List<FrontCommentVO> list = commentService.selectForUser(comment);
        return R.ok().put("data",list);
    }
    //查询评论总数
    @GetMapping("/selectCount")
    public R selectCount(@RequestParam("fid") Integer fid) {
        Long count = commentService.selectCount(fid);
        return R.ok().put("data",count);
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public R add(@RequestBody Comment comment,@CookieValue("token") String token) {

        commentService.add(comment,token);
        return R.ok();
    }
}
