package com.market.ecommerce.domain.product.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductStatus {
    AVAILABLE("판매 중"),
    OUT_OF_STOCK("재고 없음"),
    SOLD_OUT("판매 완료");

    private final String description;
}
