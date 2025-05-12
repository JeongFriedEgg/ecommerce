package com.market.ecommerce.common.exception.s3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class S3CustomException extends RuntimeException {
    private final S3ErrorCode errorCode;
    private final String originalMessage;

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}
