package com.market.ecommerce.exception.domain.product;

import com.market.ecommerce.exception.ErrorResponse;
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
public class ProductExceptionHandler {

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorResponse> handleProductException(ProductException e) {
        return buildErrorResponse(e.getErrorCode());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(ProductErrorCode errorCode) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getStatusCode())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }
}
