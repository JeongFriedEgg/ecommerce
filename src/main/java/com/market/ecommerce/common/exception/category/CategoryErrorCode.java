package com.market.ecommerce.common.exception.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CategoryErrorCode {

    INVALID_CATEGORY(HttpStatus.BAD_REQUEST,"유효하지 않은 카테고리입니다.")
    ;

    private final HttpStatus status;
    private final String message;

    public int getStatusCode() {
        return this.status.value();
    }
}
