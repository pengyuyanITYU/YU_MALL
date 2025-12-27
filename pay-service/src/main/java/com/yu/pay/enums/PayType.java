package com.yu.pay.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum PayType{
    ALI_PAY(1, "支付宝"),
    WE_CHAT(2, "微信"),
    BALANCE(3, "余额支付"),
    ;
    @JsonValue
    @EnumValue
    private final int value;
    private final String desc;

    PayType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public boolean equalsValue(Integer value){
        if (value == null) {
            return false;
        }
        return getValue() == value;
    }
}
