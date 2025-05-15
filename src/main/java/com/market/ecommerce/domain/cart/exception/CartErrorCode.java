package com.market.ecommerce.domain.cart.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CartErrorCode {

    OUT_OF_STOCK(HttpStatus.BAD_REQUEST,"재고 수량을 초과했습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    public int getStatusCode() {
        return this.status.value();
    }
}
