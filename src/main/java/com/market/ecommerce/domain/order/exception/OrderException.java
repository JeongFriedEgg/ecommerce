package com.market.ecommerce.domain.order.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderException extends RuntimeException {
    private final OrderErrorCode errorCode;
    private final List<String> details;

    public OrderException(OrderErrorCode errorCode, List<String> details) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = details;
    }

    @Override
    public String getMessage() {
      return errorCode.getMessage();
    }
}
