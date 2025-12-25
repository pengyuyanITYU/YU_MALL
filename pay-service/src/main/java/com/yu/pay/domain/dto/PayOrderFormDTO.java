package com.yu.pay.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@ApiModel(description = "支付确认表单实体")
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderFormDTO {

    @ApiModelProperty(value = "业务订单id", required = true)
    @NotNull(message = "业务订单id不能为空")
    private Long bizOrderNo;

    @ApiModelProperty("支付密码")
    @NotNull(message = "支付密码")
    private String payPassword;

    @ApiModelProperty("支付方式")
    @NotNull(message = "支付方式")
    private Integer paymentType;  // 1、支付宝，2、微信，3、扣减余额

    @ApiModelProperty("支付金额")
    @NotNull(message = "支付金额")
    private Long amount;



}