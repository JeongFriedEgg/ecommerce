package com.market.ecommerce.domain.payment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TossCancelPaymentException extends RuntimeException {
    private final TossCancelPaymentErrorCode errorCode;

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}
