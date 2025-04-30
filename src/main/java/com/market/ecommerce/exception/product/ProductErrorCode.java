package com.market.ecommerce.exception.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProductErrorCode {

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 상품입니다."),
    PRODUCT_ACCESS_DENIED(HttpStatus.FORBIDDEN,"상품에 대한 권한이 없습니다."),

    INVALID_CATEGORY(HttpStatus.BAD_REQUEST,"유효하지 않은 카테고리입니다."),
    INVALID_PRODUCT_STATUS(HttpStatus.BAD_REQUEST,"유효하지 않은 상품 상태입니다."),
    ;

    private final HttpStatus status;
    private final String message;

    public int getStatusCode() {
        return this.status.value();
    }
}
