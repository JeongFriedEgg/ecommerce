package com.market.ecommerce.domain.product.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProductErrorCode {

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 상품을 찾을 수 없습니다."),
    UNAUTHORIZED_PRODUCT_ACCESS(HttpStatus.UNAUTHORIZED,"해당 상품에 대한 권한이 없습니다."),

    PRODUCT_REGISTRATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"상품 등록 중에 에러가 발생하였습니다.")
    ;

    private final HttpStatus status;
    private final String message;

    public int getStatusCode() {
        return this.status.value();
    }
}
