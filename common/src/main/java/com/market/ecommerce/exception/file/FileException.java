package com.market.ecommerce.exception.file;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FileException extends RuntimeException {
    private final FileErrorCode errorCode;

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}
