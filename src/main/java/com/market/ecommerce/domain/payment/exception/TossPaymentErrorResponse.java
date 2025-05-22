package com.market.ecommerce.domain.payment.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TossPaymentErrorResponse {
    private int status;
    private String code;
    private String message;
}
