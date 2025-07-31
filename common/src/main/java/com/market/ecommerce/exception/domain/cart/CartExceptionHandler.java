package com.market.ecommerce.exception.domain.cart;

import com.market.ecommerce.exception.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CartExceptionHandler {

    @ExceptionHandler(CartException.class)
    public ResponseEntity<ErrorResponse> handleCartException(CartException e) {
        return buildErrorResponse(e.getErrorCode());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(CartErrorCode errorCode) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getStatusCode())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }
}
