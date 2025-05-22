package com.market.ecommerce.domain.payment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TossGetPaymentException extends RuntimeException {
    private final TossGetPaymentErrorCode errorCode;

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}
