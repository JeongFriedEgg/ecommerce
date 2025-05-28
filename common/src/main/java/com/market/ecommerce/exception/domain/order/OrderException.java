package com.market.ecommerce.exception.domain.order;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderException extends RuntimeException {
    private final OrderErrorCode errorCode;
    private final List<String> details;

    public OrderException(OrderErrorCode errorCode) {
        this.errorCode = errorCode;
        this.details = new ArrayList<>();
    }

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
