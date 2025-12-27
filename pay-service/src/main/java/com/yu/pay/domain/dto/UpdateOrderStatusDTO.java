package com.yu.pay.domain.dto;

import com.yu.api.enums.OrderStatus;
import com.yu.api.enums.PayType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateOrderStatusDTO {

    @NotNull(message = "订单ID不能为空")
    private Long id;

    @NotNull(message = "订单状态不能为空")
    private OrderStatus status;

    @NotNull(message = "支付类型不能为空")
    private PayType paymentType;
}
