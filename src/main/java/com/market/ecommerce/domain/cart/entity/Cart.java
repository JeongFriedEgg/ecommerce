package com.market.ecommerce.domain.cart.entity;

import com.market.ecommerce.domain.product.type.ProductType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RedisHash("cart")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    private String customerId;

    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class CartItem {
        @EqualsAndHashCode.Include
        private Long productId;
        private String title;
        private String mainImageUrl;
        private Integer price;
        private Integer quantity;
        private ProductType status;
    }
}