package com.market.ecommerce.common.exception.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserException extends RuntimeException {
    private final UserErrorCode errorCode;

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}
