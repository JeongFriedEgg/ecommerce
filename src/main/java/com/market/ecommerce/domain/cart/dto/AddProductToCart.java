package com.market.ecommerce.domain.cart.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.market.ecommerce.domain.cart.entity.Cart;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

public class AddProductToCart {
    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Request {
        private Long productId;
        private String title;
        private String mainImageUrl;
        private Integer price;
        private Integer quantity; // 고객이 장바구니에 상품을 추가하려는 개수를 의미
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Response {
        private Set<CartItemResponse> cartItems;

        @Getter
        @Builder
        public static class CartItemResponse {
            private Long productId;
            private String title;
            private String mainImageUrl;
            private Integer price;
            private Integer quantity;
        }

        public static Response from(Cart cart) {
            return Response.builder()
                    .cartItems(cart.getCartItems().stream()
                            .map(item -> CartItemResponse.builder()
                                    .productId(item.getProductId())
                                    .title(item.getTitle())
                                    .mainImageUrl(item.getMainImageUrl())
                                    .price(item.getPrice())
                                    .quantity(item.getQuantity())
                                    .build()
                            )
                            .collect(Collectors.toSet()))
                    .build();
        }
    }
}
