package com.yu.order.domain.dto;

import com.yu.order.domain.enums.OrderStatus;
import com.yu.order.domain.enums.PayType;
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
