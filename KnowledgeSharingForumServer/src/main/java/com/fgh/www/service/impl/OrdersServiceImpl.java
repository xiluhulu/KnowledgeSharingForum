package com.fgh.www.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.mapper.UserMapper;
import com.fgh.www.pojo.Orders;
import com.fgh.www.mapper.OrdersMapper;
import com.fgh.www.pojo.User;
import com.fgh.www.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fgh.www.service.IUserService;
import com.fgh.www.vo.user.MyOrdersVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author fgh
 * @since 2025-02-22
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {
    @Autowired
    private UserMapper userMapper;
    // 检查是否已经有相同类型订单
    @Override
    public boolean checkForOrdersOfTheSameType(Orders orders) {
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, orders.getUserId())
                        .eq(Orders::getProductName, orders.getProductName())
                                .eq(Orders::getStatus, 0);
        Orders orders1 = baseMapper.selectOne(wrapper);
        return orders1!=null;
    }

    @Override
    public Orders selectOrderNo(String orderNo) {
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getOrderNo, orderNo);
        Orders orders = baseMapper.selectOne(wrapper);
        return orders;
    }
//     修改和支付宝流水号订单状态
//    修改用户vip
    @Transactional
    @Override
    public void setAlipaySerialNumberAndOrderStatus(String trade_no,String tradeNo) {
//        修改和支付宝流水号订单状态
        LambdaUpdateWrapper<Orders> wrapper = new LambdaUpdateWrapper<Orders>();
                wrapper.eq(Orders::getOrderNo, tradeNo)
                .set(Orders::getAlipayTraceNo, trade_no)
                .set(Orders::getStatus, 1)
                        .set(Orders::getPayTime, DateUtil.now());
        baseMapper.update(wrapper);

        LambdaQueryWrapper<Orders> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Orders::getOrderNo, tradeNo);
        Orders orders = baseMapper.selectOne(wrapper1);


        //        支付成功后修改用户的vip
        LambdaUpdateWrapper<User> userWrapper = new LambdaUpdateWrapper<>();
        String nextMonth = DateUtil.format(DateUtil.nextMonth(), "yyyy-MM-dd");
        userWrapper.eq(User::getId,orders.getUserId())
                .set(User::getVipStatus,1)
                .set(User::getVipExpirationTime,nextMonth);
        userMapper.update(userWrapper);

    }
//     分页查询
    @Override
    public Page<MyOrdersVO> selectPage(Orders orders, Integer pageNum, Integer pageSize) {
        MPJLambdaWrapper<Orders> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(Orders.class)
                .leftJoin(User.class, User::getId, Orders::getUserId)
                .selectAs(User::getName, MyOrdersVO::getUserName)
                .eq(Orders::getUserId, orders.getUserId()).orderByDesc(Orders::getId);

        Page<MyOrdersVO> page = baseMapper.selectJoinPage(new Page<>(pageNum, pageSize), MyOrdersVO.class, wrapper);
        return page;
    }
//     取消订单
    @Override
    public boolean cancelOrder(Orders orders) {
        LambdaUpdateWrapper<Orders> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Orders::getOrderNo, orders.getOrderNo())
                .set(Orders::getStatus, 2);
        int update = baseMapper.update(wrapper);
        return update != 0;
    }
//     管理员分页查询
    @Override
    public Page<MyOrdersVO> adminSelectPage(Integer pageNum, Integer pageSize,Orders orders) {
        MPJLambdaWrapper<Orders> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(Orders.class)
                .leftJoin(User.class, User::getId, Orders::getUserId)
                .selectAs(User::getName, MyOrdersVO::getUserName)
                .orderByDesc(Orders::getId);
        if(StringUtils.isNoneBlank(orders.getOrderNo())){
            wrapper.like(Orders::getOrderNo, orders.getOrderNo())
                    .or()
                    .like(Orders::getAlipayTraceNo,orders.getOrderNo());
        }

        Page<MyOrdersVO> page = baseMapper.selectJoinPage(new Page<>(pageNum, pageSize), MyOrdersVO.class, wrapper);
        return page;
    }
}
