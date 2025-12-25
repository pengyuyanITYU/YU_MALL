package com.yu.user.domain.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("扣减用户余额")
public class DeductLocalMoneyDTO {

    @ApiModelProperty("用户支付密码")
    private String payPassword;

    @ApiModelProperty("金额")
    private Long amount;

    @ApiModelProperty("订单ID")
    private Long orderBizId;

    @ApiModelProperty("支付用户ID")
    @NotNull(message = "支付用户ID")
    private Long userId;

}
