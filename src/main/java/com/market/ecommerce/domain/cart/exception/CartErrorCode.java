package com.market.ecommerce.domain.cart.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CartErrorCode {

    OUT_OF_STOCK(HttpStatus.BAD_REQUEST,"재고 수량을 초과했습니다."),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND,"장바구니가 존재하지 않습니다."),
    PRODUCT_NOT_IN_CART(HttpStatus.BAD_REQUEST,"장바구니에 해당 상품이 없습니다."),
    CART_IS_EMPTY(HttpStatus.OK,"장바구니가 비어 있습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    public int getStatusCode() {
        return this.status.value();
    }
}
