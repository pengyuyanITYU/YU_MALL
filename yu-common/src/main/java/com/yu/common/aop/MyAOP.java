package com.yu.common.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
@Slf4j
public class MyAOP {


    @Pointcut("execution(* com.yu.*.controller.*.*(..))")
    private void  recordExecutionProgress(){}

    @Around("recordExecutionProgress()")
    public void recordExecution(ProceedingJoinPoint joinPoint){

//        log.info("开始执行方法：{}", joinPoint.getSignature().getName());
//        try {
//            joinPoint.proceed();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        log.info("结束执行方法：{}", joinPoint.getSignature().getName());

    }
}
