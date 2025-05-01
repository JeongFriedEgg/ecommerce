package com.market.ecommerce.product.dto;

import com.market.ecommerce.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

public class StockGet {

    @Getter
    public static class Request {
        private Long productId;
    }

    @Getter
    @Builder
    public static class Response {
        private Long productId;
        private Integer stock;

        public static Response from(Product product) {
            return Response.builder()
                    .productId(product.getId())
                    .stock(product.getStock())
                    .build();
        }
    }
}
