package com.market.ecommerce.common.exception.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderException extends RuntimeException {
    private final OrderErrorCode errorCode;

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}
