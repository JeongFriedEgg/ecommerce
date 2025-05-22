package com.market.ecommerce.domain.payment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TossConfirmPaymentException extends RuntimeException {
    private final TossConfirmPaymentErrorCode errorCode;

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}
