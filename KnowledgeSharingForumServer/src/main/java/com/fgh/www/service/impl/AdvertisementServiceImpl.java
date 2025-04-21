package com.fgh.www.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.pojo.Advertisement;
import com.fgh.www.mapper.AdvertisementMapper;
import com.fgh.www.service.IAdvertisementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 广告表 服务实现类
 * </p>
 *
 * @author fgh
 * @since 2025-02-25
 */
@Service
public class AdvertisementServiceImpl extends ServiceImpl<AdvertisementMapper, Advertisement> implements IAdvertisementService {

    @Override
    public Page<Advertisement> selectPage(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Advertisement> wrapper = new LambdaQueryWrapper<>();
        Page<Advertisement> page = baseMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return page;
    }

    @Override
    public Page<Advertisement> indexSelectPage(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Advertisement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Advertisement::getAdvertisementType, "主页广告");
        Page<Advertisement> page = baseMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return page;
    }

    @Override
    public long indexCount() {
        LambdaQueryWrapper<Advertisement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Advertisement::getAdvertisementType, "主页广告");
        return baseMapper.selectCount(wrapper);
    }

    @Override
    public long personCount() {
        LambdaQueryWrapper<Advertisement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Advertisement::getAdvertisementType, "个人中心广告");
        return baseMapper.selectCount(wrapper);

    }

    @Override
    public Page<Advertisement> personSelectPage(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Advertisement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Advertisement::getAdvertisementType, "个人中心广告");
        Page<Advertisement> page = baseMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return page;
    }

    @Override
    public List<Advertisement> selectIndex() {
        LambdaQueryWrapper<Advertisement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Advertisement::getAdvertisementType, "主页广告");
        List<Advertisement> advertisements = baseMapper.selectList(wrapper);
        return advertisements;
    }

    @Override
    public List<Advertisement> selectPrson() {
        LambdaQueryWrapper<Advertisement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Advertisement::getAdvertisementType, "个人中心广告");
        List<Advertisement> advertisements = baseMapper.selectList(wrapper);
        return advertisements;
    }
}
