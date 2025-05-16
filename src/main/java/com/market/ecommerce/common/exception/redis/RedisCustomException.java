package com.market.ecommerce.common.exception.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RedisCustomException extends RuntimeException {
    private final RedisErrorCode errorCode;

    @Override
    public String getMessage() {
    return errorCode.getMessage();
  }
}
