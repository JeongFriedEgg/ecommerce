package com.market.ecommerce.domain.product.dto;

import com.market.ecommerce.domain.product.entity.Product;
import lombok.Builder;
import lombok.Getter;

public class StockUpdate {

    @Getter
    public static class Request {
        private Long productId;
        private Integer quantity;
    }

    @Getter
    @Builder
    public static class Response {
        private Long productId;
        private Integer stock;

        public static Response fromEntity(Product product) {
            return Response.builder()
                    .productId(product.getId())
                    .stock(product.getStock())
                    .build();
        }
    }
}
