package com.fgh.www.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.common.util.R;
import com.fgh.www.pojo.Category;
import com.fgh.www.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 分类
 * </p>
 *
 * @author fgh
 * @since 2024-12-19
 */
@RestController
@RequestMapping("/admin-api/category")
public class ACategoryController {
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    // 定义缓存键
    private String cacheKey = "categories:all";
    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public R selectPage(Category category,
                        @RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum,
                        @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize) {
        Page<Category> page = categoryService.selectPage(category, pageNum, pageSize);
        return R.ok("成功").put("data", page);
    }
    /**
     * 批量删除
     */
    @DeleteMapping("/deleteBatch")
    public R deleteBatch(@RequestBody List<Integer> ids) {
        boolean b = categoryService.removeBatchByIds(ids);
        if (!b){
            return R.error("删除失败");
        }
        redisTemplate.delete(cacheKey);
        return R.ok("成功");
    }
    /**
     * 新增
     */
    @PostMapping("/add")
    public R add(@RequestBody Category category) {
        boolean b = categoryService.save(category);
        if (!b){
            return R.error("新增失败");
        }
        redisTemplate.delete(cacheKey);
        return R.ok("成功");
    }
    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public R deleteById(@PathVariable(name = "id") Integer id) {
        boolean b = categoryService.removeById(id);
        if (!b){
            return R.error("删除失败");
        }
        redisTemplate.delete(cacheKey);
        return R.ok("成功");
    }
    /**
     * 修改
     */
    @PutMapping("/update")
    public R updateById(@RequestBody Category category) {

        boolean b = categoryService.updateById(category);
        if (!b){
            return R.error("修改失败");
        }
        redisTemplate.delete(cacheKey);
        return R.ok("成功");
    }
    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public R selectAll() {


        // 尝试从缓存中获取数据
        List<Category> list = (List<Category>) redisTemplate.opsForValue().get(cacheKey);

        if (list == null) {
            // 如果缓存中没有数据，则从数据库中查询
            list = categoryService.list();

            // 将查询结果存入缓存，设置过期时间为1小时
            redisTemplate.opsForValue().set(cacheKey, list, 3600, TimeUnit.SECONDS);
        }

        return R.ok("成功").put("data", list);
    }
}
