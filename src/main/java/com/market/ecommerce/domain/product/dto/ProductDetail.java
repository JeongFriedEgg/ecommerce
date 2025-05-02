package com.market.ecommerce.domain.product.dto;

import com.market.ecommerce.domain.product.entity.Product;
import lombok.Builder;

public class ProductDetail {

    @Builder
    public static class Response {
        private Long productId;
        private String sellerId;
        private String category;
        private String name;
        private String description;
        private Integer price;
        private Integer stock;
        private String status;

        public static Response fromEntity(Product product) {
            return Response.builder()
                    .productId(product.getId())
                    .sellerId(product.getUser().getUserId())
                    .category(product.getCategory().getName())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .stock(product.getStock())
                    .status(product.getStatus().getDescription())
                    .build();
        }
    }
}
