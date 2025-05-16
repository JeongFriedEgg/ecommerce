package com.market.ecommerce.common.exception.redis.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class RedisExceptionHandlerAspect {

    @Pointcut("execution(public * com.market.ecommerce.common.client.redis..*(..))")
    public void redisClientMethods() {}

    @AfterThrowing(pointcut = "redisClientMethods()", throwing = "ex")
    public void logRedisClientExceptions(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().toShortString();
        log.error("Redis 예외 발생 - 메서드 : {}, 메시지: {}", methodName, ex.getMessage(), ex);
    }
}
