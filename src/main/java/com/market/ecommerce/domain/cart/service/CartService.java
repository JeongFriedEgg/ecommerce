package com.market.ecommerce.domain.cart.service;

import com.market.ecommerce.common.client.redis.RedisClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final RedisClient redisClient;

    public void clearCart(String customerId) {
        redisClient.deleteValue(customerId);
    }
}
