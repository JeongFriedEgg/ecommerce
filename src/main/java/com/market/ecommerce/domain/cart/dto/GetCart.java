package com.market.ecommerce.domain.cart.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
        }
    }
}
