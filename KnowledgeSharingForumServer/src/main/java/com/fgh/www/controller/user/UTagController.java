package com.fgh.www.controller.user;


import com.fgh.www.common.util.R;
import com.fgh.www.pojo.Tag;
import com.fgh.www.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 标签表 前端控制器
 * </p>
 *
 * @author fgh
 * @since 2025-02-25
 */
@RestController
@RequestMapping("/user-api/tag")
public class UTagController {
    @Autowired
    private ITagService tagService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    // 定义缓存键
    private String cacheKey = "tag:all";
    @GetMapping("/getTagList")
    public R getTagList(){

        List<Tag> tags = (List<Tag>) redisTemplate.opsForValue().get(cacheKey);
        if (tags == null) {
            // 如果缓存中没有数据，则直接返回
            tags = tagService.list();
            // 将查询结果存入缓存，设置过期时间为24小时
            redisTemplate.opsForValue().set(cacheKey, tags, 3600*24, TimeUnit.SECONDS);
        }
        return R.ok().put("data",tags);
        // 将查询结果存入缓存，设置过期时间为1小时

    }
}
