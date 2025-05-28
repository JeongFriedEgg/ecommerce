package com.market.ecommerce.exception.domain.payment;

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
