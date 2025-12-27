package com.yu.order.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(description = "交易下单表单实体")
public class OrderFormDTO {

    @ApiModelProperty(value = "收货地址id", required = true)
    @NotNull(message = "收货地址不能为空")
    private Long addressId;

    @ApiModelProperty(value = "支付类型：1、支付宝，2、微信，3、扣减余额", required = true)
    @NotNull(message = "支付类型不能为空")
    @Min(value = 1, message = "支付类型非法")
    private Integer paymentType;

    @ApiModelProperty(value = "下单商品列表", required = true)
    @NotEmpty(message = "订单商品不能为空")
    @Valid // 开启嵌套校验
    private List<OrderDetailDTO> details;

    @ApiModelProperty(value = "防重令牌", notes = "用于防止网络延迟导致的重复提交")
    private String token;

    @NotNull(message = "总金额不能为空")
    @ApiModelProperty(value = "订单总金额", required = true)
    private  Long totalFee;
}