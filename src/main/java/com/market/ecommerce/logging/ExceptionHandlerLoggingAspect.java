package com.market.ecommerce.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Aspect
@Component
@Slf4j
public class ExceptionHandlerLoggingAspect {

    @Around("@annotation(exceptionHandler)")
    public Object logExceptionHandler(ProceedingJoinPoint joinPoint, ExceptionHandler exceptionHandler) throws Throwable {
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof Exception ex) {
                log.error("[ExceptionHandler 동작] 예외 클래스: {}, 메시지: {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
            }
        }

        return joinPoint.proceed();
    }
}
