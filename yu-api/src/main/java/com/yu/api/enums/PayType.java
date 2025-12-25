package com.yu.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum PayType {
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
    /**
     * 根据支付类型代码获取对应的枚举值
     * @param value 支付类型代码
     * @return 对应的PayType枚举值
     * @throws IllegalArgumentException 如果找不到匹配的支付类型
     */
    public static PayType of(Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("支付类型代码不能为null");
        }

        for (PayType payType : PayType.values()) {
            if (payType.getValue() == value) {
                return payType;
            }
        }

        throw new IllegalArgumentException("无效的支付类型代码: " + value);
    }

    public boolean equalsValue(Integer value){
        if (value == null) {
            return false;
        }
        return getValue() == value;
    }
}
