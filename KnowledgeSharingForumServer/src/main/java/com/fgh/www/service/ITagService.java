package com.fgh.www.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.pojo.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 标签表 服务类
 * </p>
 *
 * @author fgh
 * @since 2025-02-25
 */
public interface ITagService extends IService<Tag> {
//分页查询标签
    Page<Tag> selectPage(Integer pageNum, Integer pageSize);
}
