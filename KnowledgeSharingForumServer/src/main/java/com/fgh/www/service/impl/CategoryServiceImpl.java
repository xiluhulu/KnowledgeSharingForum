package com.fgh.www.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.pojo.Category;
import com.fgh.www.mapper.CategoryMapper;
import com.fgh.www.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 博客分类 服务实现类
 * </p>
 *
 * @author fgh
 * @since 2024-12-19
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Override
    public Page<Category> selectPage(Category category, Integer pageNum, Integer pageSize) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        if(StringUtils.isNoneBlank(category.getName())){
            wrapper.like("name",category.getName());

        }
        return this.page(new Page<Category>(pageNum,pageSize),wrapper);
    }
}
