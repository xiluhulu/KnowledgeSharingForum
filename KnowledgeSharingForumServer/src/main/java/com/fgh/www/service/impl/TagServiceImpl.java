package com.fgh.www.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.pojo.Tag;
import com.fgh.www.mapper.TagMapper;
import com.fgh.www.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author fgh
 * @since 2025-02-25
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {
//分页查询标签
    @Override
    public Page<Tag> selectPage(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        Page<Tag> page = baseMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return page;
    }
}
