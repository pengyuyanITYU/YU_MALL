package com.yu.order.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`order`") // 对应数据库 order 表
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id (雪花算法)
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 总金额，单位为分
     */
    private Long totalFee;

    /**
     * 支付类型：1、支付宝，2、微信，3、扣减余额
     */
    private Integer paymentType;

    /**
     * 订单状态：1、未付款 2、已付款,未发货 3、已发货,未确认 4、交易成功 5、交易取消 6、已评价
     */
    private Integer status;

    // ==========================================
    // 地址快照 (即使地址簿修改，订单地址也不变)
    // ==========================================

    /**
     * 地址薄ID (仅作关联参考)
     */
    private Long addressId;


    /**
     * 收货人姓名
     */
    private String receiverContact;

    /**
     * 收货人手机号
     */
    private String receiverMobile;

    /**
     * 收货详细地址
     */
    private String receiverAddress;

    // ==========================================
    // 时间字段
    // ==========================================

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;



    private LocalDateTime payTime;

    private LocalDateTime consignTime;

    private LocalDateTime endTime;

    private LocalDateTime closeTime;

    private LocalDateTime commentTime;



    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;




}