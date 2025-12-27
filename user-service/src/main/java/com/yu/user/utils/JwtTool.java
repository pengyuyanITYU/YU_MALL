package com.yu.user.utils;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtTool {
    private final JWTSigner jwtSigner;

    public JwtTool(KeyPair keyPair) {
        this.jwtSigner = JWTSignerUtil.createSigner("rs256", keyPair);
    }

    public  String  createToken(Long userId, Duration ttl) {
        return JWT.create()
                .setPayload("user", userId)
                .setExpiresAt(new Date(System.currentTimeMillis() + ttl.toMillis()))
                .setSigner(jwtSigner)
                .sign();
    }

    public Long parseToken(String token) {
        // 1. 基础非空校验
        if (StrUtil.isBlank(token)) {
            throw new RuntimeException("未登录");
        }

        JWT jwt;
        try {
            // 2. 解析并设置签名器（此时未验证签名，仅解析结构）
            jwt = JWT.of(token).setSigner(jwtSigner);
        } catch (Exception e) {
            throw new RuntimeException("无效的token", e);
        }

        // 3. 校验签名（验证Token是否被篡改）
        if (!jwt.verify()) {
            throw new RuntimeException("无效的token");
        }

        // 4. 校验有效期
        try {
            // 0 表示允许的时间误差为0秒
            JWTValidator.of(jwt).validateDate(new Date(), 0);
        } catch (ValidateException e) {
            throw new RuntimeException("token已经过期");
        }

        // 5. 获取数据（类型安全的方式）
        Long userId = jwt.getPayloads().getLong("user");
        if (userId == null) {
            throw new RuntimeException("无效的token");
        }

        return userId;
    }
}