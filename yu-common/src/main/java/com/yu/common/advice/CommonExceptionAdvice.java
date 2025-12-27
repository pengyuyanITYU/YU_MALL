package com.yu.common.advice;


import com.yu.common.domain.AjaxResult;
import com.yu.common.domain.R;
import com.yu.common.exception.BadRequestException;
import com.yu.common.exception.BusinessException;
import com.yu.common.exception.CommonException;
import com.yu.common.exception.DbException;
import com.yu.common.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.NestedServletException;

import java.net.BindException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class CommonExceptionAdvice {

    @ExceptionHandler(DbException.class)
    public AjaxResult handleDbException(DbException e) {
        log.error("mysql数据库操作异常 -> ", e);
        return AjaxResult.error(e.getMessage());
    }

    @ExceptionHandler(CommonException.class)
    public AjaxResult handleBadRequestException(CommonException e) {
        log.error("自定义异常 -> {} , 异常原因：{}  ",e.getClass().getName(), e.getMessage());
        log.debug("", e);
        return AjaxResult.error(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getAllErrors()
                .stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("|"));
        log.error("请求参数校验异常 -> {}", msg);
        log.debug("", e);
        return AjaxResult.error(msg);
    }
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e) {
        log.error("请求参数绑定异常 ->BindException， {}", e.getMessage());
        log.debug("", e);
       return AjaxResult.error(e.getMessage());
    }

    @ExceptionHandler(NestedServletException.class)
    public AjaxResult handleNestedServletException(NestedServletException e) {
        log.error("参数异常 -> NestedServletException，{}", e.getMessage());
        log.debug("", e);
        return AjaxResult.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public AjaxResult  handleRuntimeException(Exception e) {
        log.error("其他异常 uri : {} -> ", WebUtils.getRequest().getRequestURI(), e);
        return AjaxResult.error(e.getMessage());
    }

    private ResponseEntity<R<Void>> processResponse(CommonException e){
        return ResponseEntity.status(e.getCode()).body(R.error(e));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public AjaxResult handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return AjaxResult.error(ex.getMessage());
    }

    @ExceptionHandler(value = BusinessException.class)
    public AjaxResult handleBusinessException(BusinessException e) {
        log.error("业务异常 -> {}", e.getMessage());
        log.debug("", e);
        return AjaxResult.error(e.getMessage());
    }


}

