package com.fgh.www.controller.user;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.common.util.Jwt_Utils;
import com.fgh.www.common.util.R;
import com.fgh.www.pojo.Orders;
import com.fgh.www.pojo.User;
import com.fgh.www.service.IOrdersService;
import com.fgh.www.service.IUserService;
import com.fgh.www.vo.user.MyOrdersVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author fgh
 * @since 2025-02-22
 */
@Tag(name = "前端Orders接口", description = "包含用户相关的 API 接口")
@RestController
@RequestMapping("/user-api/orders")
public class UOrdersController {
    @Autowired
    private IOrdersService ordersService;
    @Autowired
    private IUserService userService;

    //创建订单
    @PostMapping("/createVipOrder")
    public R createVipOrder(@CookieValue("token") String token) {

        //获取用户id
        Integer userId = Jwt_Utils.getId(token);
        User user = userService.getById(userId);
        if (user.getVipStatus() == 1) {
            return R.error("您已经是会员了");
        }
        Orders orders = new Orders();
        //设置买家id
        orders.setUserId(userId);
        //设置订单商品名称
        orders.setProductName("vip");
        //查询是否有会员订单，如果有则返回存在相同类型订单
        boolean b = ordersService.checkForOrdersOfTheSameType(orders);
        if (b) {
            return R.error("存在相同类型订单,请到个人中心支付订单");
        }
        //设置订单号
        orders.setOrderNo(UUID.randomUUID().toString() + userId + System.currentTimeMillis());
        //设置订单金额
        orders.setAmountPaid(5f);
        //设置订单状态
        orders.setStatus(0);
        //设置订单创建时间
        orders.setCreateTitme(DateUtil.now());
        //保存订单
        ordersService.save(orders);
        Orders orders1 = ordersService.selectOrderNo(orders.getOrderNo());
        return R.ok().put("data", orders1);
    }
    //取消订单
    @PostMapping("/cancelOrder")
    public R cancelOrder(@RequestBody Orders orders, @CookieValue("token") String token) {
        boolean b = ordersService.cancelOrder(orders);
        if (!b) {
            return R.error("取消订单失败");
        }
        return R.ok("取消成功");


    }
    //分页查询订单
    @GetMapping("/selectPage")
    public R selectPage(@RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum,
                        @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize,
                        @CookieValue("token") String token) {
        //获取用户id
        Integer userId = Jwt_Utils.getId(token);
        Orders orders = new Orders();
        orders.setUserId(userId);
        Page<MyOrdersVO> page = ordersService.selectPage(orders, pageNum, pageSize);
        return R.ok().put("data", page);
    }
}
