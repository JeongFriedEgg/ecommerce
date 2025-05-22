package com.market.ecommerce.domain.cart.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.market.ecommerce.domain.product.service.dto.ProductValidationResult;
import com.market.ecommerce.domain.product.type.ProductType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class GetCart {
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Response {
        private List<CartItem> cartItems;
        private List<String> messages;

        @Getter
        @Builder
        public static class CartItem {
            private Long productId;
            private String title;
            private String mainImageUrl;
            private Integer price;
            private Integer quantity;
            private ProductType status;
        }

        public static List<CartItem> fromProductItems(
                List<ProductValidationResult.ProductItem> productItems
        ) {
            return productItems.stream()
                    .map(item -> CartItem.builder()
                            .productId(item.getProductId())
                            .title(item.getTitle())
                            .mainImageUrl(item.getMainImageUrl())
                            .price(item.getPrice())
                            .quantity(item.getQuantity())
                            .status(item.getStatus())
                            .build()
                    ).toList();
        }
    }
}
