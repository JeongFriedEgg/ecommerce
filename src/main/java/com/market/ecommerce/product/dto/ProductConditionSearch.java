package com.market.ecommerce.product.dto;

import com.market.ecommerce.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

public class ProductConditionSearch {

    @Getter
    public static class Request {
        private String keyword;
        private String category;
        private String status;
    }

    @Getter
    @Builder
    public static class Response {
        private Long id;
        private String name;
        private Integer price;
        private String status;
        private String category;

        public static Response from(Product product) {
            return Response.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .status(product.getStatus().getDescription())
                    .category(product.getCategory().getName())
                    .build();
        }
    }
}
