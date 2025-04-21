package com.fgh.www.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.pojo.Advertisement;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 广告表 服务类
 * </p>
 *
 * @author fgh
 * @since 2025-02-25
 */
public interface IAdvertisementService extends IService<Advertisement> {

    Page<Advertisement> selectPage(Integer pageNum, Integer pageSize);

    Page<Advertisement> indexSelectPage(Integer pageNum, Integer pageSize);

    long indexCount();

    long personCount();

    Page<Advertisement> personSelectPage(Integer pageNum, Integer pageSize);
//用户查寻主页广告
    List<Advertisement> selectIndex();
//用户查寻个人广告
    List<Advertisement> selectPrson();
}
