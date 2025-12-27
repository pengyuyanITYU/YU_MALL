package com.yu.common.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;

public class BusinessException extends RuntimeException {

    private  String code;

    public BusinessException(String message) {
        super(message);
    }
    public BusinessException(String message, String code) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }
    public String getCode() {
        return code;
    }
}
