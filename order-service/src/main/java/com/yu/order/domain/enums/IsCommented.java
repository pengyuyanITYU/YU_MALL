package com.yu.order.domain.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum IsCommented {

    COMMENTED(true, "已评价"),
    NOT_COMMENTED(false, "未评价");

    private final boolean value;
    private final String desc;


}
