package com.market.ecommerce.domain.cart.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.Set;

@RedisHash("cart")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    private Long customerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<CartItem> cartItems;

    @Builder
    public static class CartItem {
        private Long productId;
        private Integer quantity;
    }
}