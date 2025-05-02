package com.market.ecommerce.domain.product.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.market.ecommerce.domain.product.entity.Product;
import lombok.Builder;
import lombok.Getter;

public class ProductRegister {

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Request {
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
        private String createdDate;

        public static Response fromEntity(Product product) {
            return Response.builder()
                    .productId(product.getId())
                    .name(product.getName())
                    .createdDate(product.getCreatedAt().toString())
                    .build();
        }
    }
}
