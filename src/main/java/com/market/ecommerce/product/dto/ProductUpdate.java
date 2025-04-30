package com.market.ecommerce.product.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.market.ecommerce.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

public class ProductUpdate {

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Request {
        private Long productId;
        private String userId;
        private String category;
        private String name;
        private String description;
        private Integer price;
        private Integer stock;
        private String status;
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Response {
        private Long productId;
        private String name;
        private String updatedDate;

        public static ProductUpdate.Response from(Product product) {
            return ProductUpdate.Response.builder()
                    .productId(product.getId())
                    .name(product.getName())
                    .updatedDate(product.getUpdatedAt().toString())
                    .build();
        }
    }
}
