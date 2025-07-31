package com.market.ecommerce.exception.domain.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TossPaymentExceptionHandler {

    @ExceptionHandler(TossGetPaymentException.class)
    public ResponseEntity<TossPaymentErrorResponse> handleTossGetPaymentException(TossGetPaymentException e) {
        TossGetPaymentErrorCode errorCode = e.getErrorCode();
        TossPaymentErrorResponse errorResponse = TossPaymentErrorResponse.builder()
                .status(errorCode.getStatusCode())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }

    @ExceptionHandler(TossConfirmPaymentException.class)
    public ResponseEntity<TossPaymentErrorResponse> handleTossConfirmPaymentException(TossConfirmPaymentException e) {
        TossConfirmPaymentErrorCode errorCode = e.getErrorCode();
        TossPaymentErrorResponse errorResponse = TossPaymentErrorResponse.builder()
                .status(errorCode.getStatusCode())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }

    @ExceptionHandler(TossCancelPaymentException.class)
    public ResponseEntity<TossPaymentErrorResponse> handleTossCancelPaymentException(TossCancelPaymentException e) {
        TossCancelPaymentErrorCode errorCode = e.getErrorCode();
        TossPaymentErrorResponse errorResponse = TossPaymentErrorResponse.builder()
                .status(errorCode.getStatusCode())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }

    @ExceptionHandler(TossPaymentException.class)
    public ResponseEntity<TossPaymentErrorResponse> handleTossPaymentException(TossPaymentException e) {
        TossPaymentErrorCode errorCode = e.getErrorCode();
        TossPaymentErrorResponse errorResponse = TossPaymentErrorResponse.builder()
                .status(errorCode.getStatusCode())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }
}
