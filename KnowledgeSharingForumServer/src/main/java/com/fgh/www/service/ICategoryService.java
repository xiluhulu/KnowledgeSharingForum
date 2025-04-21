package com.fgh.www.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 博客分类 服务类
 * </p>
 *
 * @author fgh
 * @since 2024-12-19
 */
public interface ICategoryService extends IService<Category> {

    Page<Category> selectPage(Category category, Integer pageNum, Integer pageSize);
}
