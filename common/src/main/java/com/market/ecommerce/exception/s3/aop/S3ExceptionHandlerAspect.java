package com.market.ecommerce.exception.s3.aop;

import com.market.ecommerce.exception.s3.S3CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;

import static com.market.ecommerce.exception.s3.S3ErrorCode.FILE_S3_ERROR;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class S3ExceptionHandlerAspect {

    @Around("execution(* com.market.ecommerce.common.service.S3Service.*(..))")
    public Object handleS3Exceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (S3Exception e) {
            log.error("AWS S3 예외 발생 - 메서드: {}, 메시지: {}", joinPoint.getSignature(), e.getMessage(), e);
            throw new S3CustomException(FILE_S3_ERROR, e.getMessage());
        } catch (IOException e) {
            log.error("AWS S3 입출력 예외 발생 - 메서드: {}, 메시지: {}", joinPoint.getSignature(), e.getMessage(), e);
            throw new S3CustomException(FILE_S3_ERROR, e.getMessage());
        } catch (Exception e) {
            log.error("AWS S3 처리 중 알 수 없는 예외 발생 - 메서드: {}", joinPoint.getSignature(), e);
            throw new S3CustomException(FILE_S3_ERROR, e.getMessage());
        }
    }
}
