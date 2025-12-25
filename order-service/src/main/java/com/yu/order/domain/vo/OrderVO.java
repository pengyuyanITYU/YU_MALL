package com.yu.order.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description = "订单详情视图对象")
@Accessors(chain = true)
public class OrderVO {

    @ApiModelProperty("订单id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

//    @ApiModelProperty("用户id")
//    private Long userId;

    @ApiModelProperty("总金额(元)")
    private Long totalFee;

    @ApiModelProperty("支付类型")
    private Integer paymentType;

    @ApiModelProperty("订单状态")
    private Integer status;

    @ApiModelProperty("下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("支付时间")
    private LocalDateTime payTime;

    @ApiModelProperty("发货时间")
    private LocalDateTime consignTime;

    @ApiModelProperty("完成时间")
    private LocalDateTime endTime;

    // --- 地址快照信息 ---
    @ApiModelProperty("收货人")
    private String receiverContact;

    @ApiModelProperty("手机号")
    private String receiverMobile;

    @ApiModelProperty("详细地址")
    private String receiverAddress;

    // --- 聚合的商品列表 ---
    @ApiModelProperty("订单包含的商品列表")
    private List<OrderDetailVO> details;

    @ApiModelProperty("交易关闭时间")
    private LocalDateTime closeTime;


}