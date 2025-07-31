package com.market.ecommerce.exception.domain.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CategoryException extends RuntimeException {
    private final CategoryErrorCode errorCode;

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}