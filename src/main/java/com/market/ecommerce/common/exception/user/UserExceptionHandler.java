package com.market.ecommerce.common.exception.user;

import com.market.ecommerce.common.exception.ErrorResponse;
import com.market.ecommerce.common.exception.MultiErrorResponse;
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
public class UserExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserException e) {
        return buildErrorResponse(e.getErrorCode());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(UserErrorCode errorCode) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getStatusCode())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }

    @ExceptionHandler(MultiUserException.class)
    public ResponseEntity<MultiErrorResponse> handleMultiUserConflictException(MultiUserException e) {
        List<MultiErrorResponse.ErrorDetail> errorDetails = e.getErrorCodes().stream()
                .map(code -> MultiErrorResponse.ErrorDetail.builder()
                        .code(code.name())
                        .message(code.getMessage())
                        .build())
                .toList();

        MultiErrorResponse response = MultiErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .errors(errorDetails)
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

}
