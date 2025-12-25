package com.yu.order.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 订单状态枚举
 * 实现 Valuable 接口后可以直接这样用：
 * Integer status = OrderStatus.SHIPPED;
 * if (OrderStatus.PAID == status) { ... }
 */
@AllArgsConstructor
@Getter
public enum OrderStatus  {

    UNPAID(1, "未付款"),
    PAID(2, "已付款, 未发货"),
    SHIPPED(3, "已发货, 未确认"),
    SUCCESS(4, "交易成功"),
    CANCELED(5, "交易取消"),     // 对应你图里的“交易取消”而不是“交易取消”
    EVALUATED(6, "已评价");

    @EnumValue
    @JsonValue
    private final Integer value;
    private final String desc;
    /**
     * 根据数值获取对应的 OrderStatus 对象
     *
     * @param value 状态码数值
     * @return OrderStatus 枚举对象，若未匹配则返回 null
     */
    public static OrderStatus of(Integer value) {
        if (value == null) {
            return null;
        }
        for (OrderStatus status : OrderStatus.values()) {
            if (Objects.equals(status.getValue(), value)) {
                return status;
            }
        }
        return null;
    }

    // ========== 常用业务判断方法（极大提升代码可读性）==========
    public boolean isUnpaid()   { return this == UNPAID; }
    public boolean isPaid()     { return this == PAID; }
    public boolean isShipped()  { return this == SHIPPED; }
    public boolean isSuccess()  { return this == SUCCESS; }
    public boolean isCanceled() { return this == CANCELED; }
    public boolean isEvaluated(){ return this == EVALUATED; }

    // 是否已支付（包含已发货、交易成功、已评价）
    public boolean isPaidOrLater() {
        return this == PAID || this == SHIPPED || this == SUCCESS || this == EVALUATED;
    }

    // 是否交易完成（成功 or 取消）
    public boolean isFinished() {
        return this == SUCCESS || this == CANCELED;
    }
}