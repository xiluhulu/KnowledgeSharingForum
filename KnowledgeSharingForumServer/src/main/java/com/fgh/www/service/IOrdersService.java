package com.fgh.www.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.pojo.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fgh.www.vo.user.MyOrdersVO;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author fgh
 * @since 2025-02-22
 */
public interface IOrdersService extends IService<Orders> {
    //检测是否已经存在相同类型的订单
    boolean checkForOrdersOfTheSameType(Orders orders);
//    根据订单号查询订单
    Orders selectOrderNo(String orderNo);
    //设置支付宝流水号和订单状态
    void setAlipaySerialNumberAndOrderStatus(String trade_no,String tradeNo);
    //分页查询
    Page<MyOrdersVO> selectPage(Orders orders, Integer pageNum, Integer pageSize);
//    取消订单
    boolean cancelOrder(Orders orders);
//    管理员分页查询
    Page<MyOrdersVO> adminSelectPage(Integer pageNum, Integer pageSize,Orders orders);
}
