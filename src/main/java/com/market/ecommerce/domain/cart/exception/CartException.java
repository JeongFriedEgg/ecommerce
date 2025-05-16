package com.market.ecommerce.domain.cart.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CartException extends RuntimeException {
    private final CartErrorCode errorCode;

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}
