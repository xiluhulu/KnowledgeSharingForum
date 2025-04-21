package com.fgh.www.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.common.util.R;
import com.fgh.www.pojo.Orders;
import com.fgh.www.service.IOrdersService;
import com.fgh.www.service.IUserService;
import com.fgh.www.vo.user.MyOrdersVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/admin-api/orders")
public class AOrdersController {
    @Autowired
    private IOrdersService ordersService;
    @Autowired
    private IUserService userService;


    //分页查询订单
    @GetMapping("/selectPage")
    public R selectPage(Orders orders, @RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum,
                        @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize) {
        Page<MyOrdersVO> page = ordersService.adminSelectPage( pageNum, pageSize,orders);
        return R.ok().put("data", page);
    }
}
