package com.market.ecommerce.domain.product.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductException extends RuntimeException {
    private final ProductErrorCode errorCode;

    @Override
    public String getMessage() {
    return errorCode.getMessage();
  }
}
