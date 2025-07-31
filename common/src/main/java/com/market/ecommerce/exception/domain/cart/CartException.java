package com.market.ecommerce.exception.domain.cart;

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
