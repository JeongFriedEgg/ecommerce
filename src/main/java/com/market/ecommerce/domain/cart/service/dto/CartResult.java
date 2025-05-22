package com.market.ecommerce.domain.cart.service.dto;

import com.market.ecommerce.domain.product.type.ProductType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CartResult {
    private List<CartItem> cartItems;

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
}
