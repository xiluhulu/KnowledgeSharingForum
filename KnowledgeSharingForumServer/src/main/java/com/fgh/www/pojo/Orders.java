package com.fgh.www.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author fgh
 * @since 2025-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 购买用户id
     */
    private Integer userId;

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
     * deleted逻辑删除1为删除
     */

    @TableLogic
    private Integer deleted;
    /**
     *  创建时间
     */
    private String createTitme;
    /**
     *  支付时间
     */
    private String payTime;

}
