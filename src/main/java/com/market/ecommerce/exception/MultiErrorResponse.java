package com.market.ecommerce.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MultiErrorResponse {
    private int status;
    private List<ErrorDetail> errors;

    @Getter
    @Builder
    public static class ErrorDetail {
        private String code;
        private String message;
    }
}
