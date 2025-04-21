package com.fgh.www.controller.admin;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.fgh.www.common.util.R;
import com.fgh.www.pojo.Article;
import com.fgh.www.service.IArticleService;
import com.fgh.www.vo.user.ArticleVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 文章
 * </p>
 *
 * @author fgh
 * @since 2024-12-19
 */
@Tag(name = "后台article接口", description = "包含文章相关的 API 接口")
@RestController
@RequestMapping("/admin-api/article")
public class AArticleController {
    @Autowired
    private IArticleService articleService;
    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public R selectPage(Article article, @RequestParam(name = "categoryName", required = false, defaultValue = "") String categoryName,
                        @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        System.out.println("categoryName+=+=====" + categoryName);
        Page<ArticleVO> page = articleService.selectPage(article, pageNum, pageSize, categoryName);
        return R.ok("成功").put("data", page);
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/deleteBatch")
    public R deleteBatch(@RequestBody List<Integer> ids) {
        boolean b = articleService.logicRemoveBatchByIds(ids);
        return b ? R.ok("批量删除成功") : R.error();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public R deleteById(@PathVariable(name = "id") Integer id) {
        boolean b = articleService.logicRemoveById(id);
        return b ? R.ok("删除成功") : R.error();
    }
    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public R selectById(@PathVariable(name = "id") Integer id, @RequestHeader(name = "token", defaultValue = "") String token) {
        ArticleVO article = articleService.selectGetById(id, token);
        return R.ok().put("data", article);
    }
//    /**
//     * 新增
//     */
//    @PostMapping("/add")
//    public R add(@RequestBody Article article ,@RequestHeader("token")String token) {
//        Integer id = Jwt_Utils.getId(token);
//        article.setUserId(id);
//        article.setDate(DateUtil.today());
//        boolean b = articleService.save(article);
//        if (!b) {
//            return R.error("新增失败");
//        }
//        return R.ok().put("data", article.getId());
//    }
//
//    /**
//     * 修改
//     */
//    @PutMapping("/update")
//    public R updateById(@RequestBody Article article) {
//        boolean b = articleService.updateById(article);
//        if (!b) {
//            return R.error("修改失败");
//        }
//        return R.ok();
//    }
//
//   /*
//     * 根据ID查询
//     */
//    @GetMapping("/selectById/{id}")
//    public R selectById(@PathVariable(name = "id") Integer id, @RequestHeader(name = "token",defaultValue = "") String token) {
//        ArticleVO article = articleService.selectGetById(id, token);
//        return R.ok().put("data", article);
//    }
//
////    跟新点赞数量
//    @PutMapping("/updateReadCount")
//    public R updateReadCount(@RequestBody Integer articleId) {
//        boolean b = articleService.addReadCount(articleId);
//        if (!b) {
//            return R.error("更新失败");
//        }
//        return R.ok();
//    }
//
//    /*
//     * 分页查询当前用户的文章列表
//     */
//    @GetMapping("/selectUser")
//    public R selectUser(Article article,
//                        @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
//                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
//                        @RequestHeader("token") String token) {
//        Page<ArticleVO> page = articleService.selectUserPage(article, pageNum, pageSize, token);
//        return R.ok("成功").put("data", page);
//    }
//    /*
//     * 小时榜单查询
//     */
//    @GetMapping("/selectHourRank")
//    public R selectHourRank() {
//        List<ArticleVO> list = articleService.selectHourRank();
//        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
//        stringObjectHashMap.put("records",list);
//        return R.ok("成功").put("data", list);
//    }
}
