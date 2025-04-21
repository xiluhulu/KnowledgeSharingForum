package com.fgh.www.vo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyOrdersVO {
    /**
     * id
     */
    private Integer id;

    /**
     * 商品名称
     */
    private String productName;



    /**
     * 购买用户名称
     */
    private String userName;
    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 支付金额
     */
    private Float amountPaid;

    /**
     * 订单状态（0：待支付，1：已支付，2：取消支付）
     */
    private Integer status;

    /**
     * 支付宝流水号
     */
    private String alipayTraceNo;
    /**
     *  创建时间
     */
    private String createTitme;
    /**
     *  支付时间
     */
    private String payTime;
}
