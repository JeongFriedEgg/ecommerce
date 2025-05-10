package com.market.ecommerce.domain.product.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.market.ecommerce.domain.product.entity.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

public class ProductRegister {

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Request {
        private String title;
        private String description;
        private int price;
        private int stock;
        private Set<String> categories;
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Response {
        private Long id;
        private String title;
        private String createdDate;

        public static Response fromProductEntity(Product product) {
            return Response.builder()
                    .id(product.getId())
                    .title(product.getTitle())
                    .createdDate(product.getCreatedAt().toString())
                    .build();
        }
    }
}
