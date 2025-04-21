package com.fgh.www.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 广告表
 * </p>
 *
 * @author fgh
 * @since 2025-02-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("advertisement")
public class Advertisement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 广告图像地址
     */
    private String advertisementImgUrl;

    /**
     * 广告跳转地址
     */
    private String advertisementToUrl;

    /**
     * 广告类型（主页广告，详情页广告，个人中心广告）
     */
    private String advertisementType;


}
