package com.market.ecommerce.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e) {
        return buildErrorResponse(ErrorCode.NULL_POINTER, e.getMessage());
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorResponse> handleNumberFormatException(NumberFormatException e) {
        return buildErrorResponse(ErrorCode.NUMBER_FORMAT, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return buildErrorResponse(ErrorCode.ILLEGAL_ARGUMENT, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        return buildErrorResponse(ErrorCode.RUNTIME_EXCEPTION, e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
        return buildErrorResponse(ErrorCode.ILLEGAL_STATE, e.getMessage());
    }

    @ExceptionHandler(NoSuchMethodException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchMethodException(NoSuchMethodException e) {
        return buildErrorResponse(ErrorCode.NO_SUCH_METHOD, e.getMessage());
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<ErrorResponse> handleClassCastException(ClassCastException e) {
        return buildErrorResponse(ErrorCode.CLASS_CAST, e.getMessage());
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<ErrorResponse> handleParseException(ParseException e) {
        return buildErrorResponse(ErrorCode.PARSE_EXCEPTION, e.getMessage());
    }

    @ExceptionHandler(InvocationTargetException.class)
    public ResponseEntity<ErrorResponse> handleInvocationTargetException(InvocationTargetException e) {
        return buildErrorResponse(ErrorCode.INVOCATION_TARGET, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        return buildErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(ErrorCode errorCode, String detailMessage) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getStatusCode())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .detail(detailMessage)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }
}
