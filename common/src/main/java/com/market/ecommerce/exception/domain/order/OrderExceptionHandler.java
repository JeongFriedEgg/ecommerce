package com.market.ecommerce.exception.domain.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OrderExceptionHandler {

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<OrderErrorResponse> handleProductException(OrderException e) {
        return buildErrorResponse(e.getErrorCode(), e.getDetails());
    }

    private ResponseEntity<OrderErrorResponse> buildErrorResponse(OrderErrorCode errorCode, List<String> details) {
        OrderErrorResponse errorResponse = OrderErrorResponse.builder()
                .status(errorCode.getStatusCode())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .details(details)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }
}
