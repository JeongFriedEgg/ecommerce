package com.market.ecommerce.product.dto;

import com.market.ecommerce.product.domain.Product;
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

        public static Response from(Product product) {
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
