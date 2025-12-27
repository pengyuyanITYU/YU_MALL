package com.yu.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;



// PayStatus.java
@AllArgsConstructor
@Getter
public enum PayStatus  {
    NOT_COMMIT(0, "未提交"),
    WAIT_BUYER_PAY(1, "待支付"),
    TRADE_CLOSED(2, "已关闭"),
    TRADE_SUCCESS(3, "支付成功");

    private final Integer value;
    private final String desc;



}