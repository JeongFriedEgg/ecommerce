package com.market.ecommerce.common.logging;

import com.market.ecommerce.common.exception.category.CategoryException;
import com.market.ecommerce.common.exception.order.OrderException;
import com.market.ecommerce.common.exception.product.ProductException;
import com.market.ecommerce.common.exception.user.MultiUserException;
import com.market.ecommerce.common.exception.user.UserErrorCode;
import com.market.ecommerce.common.exception.user.UserException;
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
            if (arg instanceof MultiUserException multiEx) {
                log.error("[MultiUserException 발생] 예외 클래스: {}", multiEx.getClass().getSimpleName());
                for (UserErrorCode errorCode : multiEx.getErrorCodes()) {
                    log.error("    - 코드: {}, 메시지: {}", errorCode.name(), errorCode.getMessage());
                }
            } else if (arg instanceof UserException ex){
                log.error("[UserException 발생] 예외 클래스: {}, 메시지: {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
            } else if (arg instanceof ProductException ex){
                log.error("[ProductException 발생] 예외 클래스: {}, 메시지: {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
            } else if (arg instanceof OrderException ex){
                log.error("[OrderException 발생] 예외 클래스: {}, 메시지: {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
            } else if (arg instanceof CategoryException ex){
                log.error("[CategoryException 발생] 예외 클래스: {}, 메시지: {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
            } else if (arg instanceof Exception ex) {
                log.error("[ExceptionHandler 동작] 예외 클래스: {}, 메시지: {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
            }
        }

        return joinPoint.proceed();
    }
}
