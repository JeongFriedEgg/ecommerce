package com.market.ecommerce.domain.order.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderErrorCode {

    PRODUCT_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "상품 검증에 실패하였습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    public int getStatusCode() {
        return this.status.value();
    }
}
