package com.market.ecommerce.domain.cart.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashSet;
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
    private Set<CartItem> cartItems = new HashSet<>();

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
    }
}