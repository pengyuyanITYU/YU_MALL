package com.yu.pay.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class OrderIdGenerator {

    // 修改格式：使用 yy (2位年份)，总长度 = 12位时间 + 6位随机 = 18位
    // 结果示例：251203165424123456 (完全在 Long 范围内)
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    public static String generatePayOrderNo() {
        // 1. 获取当前时间字符串 (12位)
        String timeStr = LocalDateTime.now().format(TIME_FORMATTER);

        // 2. 生成随机数 (6位)
        int randomNum = ThreadLocalRandom.current().nextInt(100000, 999999);

        // 3. 拼接
        return timeStr + randomNum;
    }
}