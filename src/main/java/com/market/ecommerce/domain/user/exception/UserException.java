package com.market.ecommerce.domain.user.exception;


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
