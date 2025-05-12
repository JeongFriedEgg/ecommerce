package com.market.ecommerce.common.exception.s3;

import com.market.ecommerce.common.exception.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class S3ExceptionHandler {

    @ExceptionHandler(S3CustomException.class)
    public ResponseEntity<ErrorResponse> handleFileException(S3CustomException e) {
        return buildErrorResponse(e.getErrorCode(), e.getOriginalMessage());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(S3ErrorCode errorCode, String originalMessage) {
        String detailMessage = extractMessageBeforeParenthesis(originalMessage);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getStatusCode())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .detail(detailMessage)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }

    private String extractMessageBeforeParenthesis(String message) {
        if (message == null) return null;
        int index = message.indexOf('(');
        return index == -1 ? message : message.substring(0, index).trim();
    }
}