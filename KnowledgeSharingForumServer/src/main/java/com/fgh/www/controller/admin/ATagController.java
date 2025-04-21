package com.fgh.www.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.common.util.R;
import com.fgh.www.service.ITagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 标签表 前端控制器
 * </p>
 *
 * @author fgh
 * @since 2025-02-25
 */
@Tag(name = "后台Tag接口", description = "包含标签相关的 API 接口")
@RestController
@RequestMapping("/admin-api/tag")
public class ATagController {
    @Autowired
    private ITagService tagService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    // 定义缓存键
    private String cacheKey = "tag:all";
    // 分页查询标签
    @GetMapping("/selectPage")
    public R selectPage(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<com.fgh.www.pojo.Tag> page = tagService.selectPage(pageNum, pageSize);
        return R.ok().put("data", page);
    }
    @PostMapping("/add")
    public R add(@RequestBody com.fgh.www.pojo.Tag tag) {
        boolean b = tagService.save(tag);
        redisTemplate.delete(cacheKey);
        return b ? R.ok("添加成功") : R.error();
    }
    @PutMapping("/update")
    public R updateById(@RequestBody com.fgh.www.pojo.Tag tag) {
        boolean b = tagService.updateById(tag);
        redisTemplate.delete(cacheKey);
        return b ? R.ok("修改成功") : R.error();
    }
    @DeleteMapping("/delete/{id}")
    public R deleteById(@PathVariable(name = "id") Integer id) {
        boolean b = tagService.removeById(id);
        redisTemplate.delete(cacheKey);
        return b ? R.ok("删除成功") : R.error();
    }
    @DeleteMapping("/deleteBatch")
    public R deleteBatch(@RequestBody List<Integer> ids) {
        boolean b = tagService.removeBatchByIds(ids);
        redisTemplate.delete(cacheKey);
        return b ? R.ok("批量删除成功") : R.error();
    }
}
