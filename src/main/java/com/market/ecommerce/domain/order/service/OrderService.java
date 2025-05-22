package com.market.ecommerce.domain.order.service;

import com.market.ecommerce.common.client.redis.RedisClient;
import com.market.ecommerce.domain.order.dto.OrderCreate;
import com.market.ecommerce.domain.order.entity.Order;
import com.market.ecommerce.domain.order.entity.OrderItem;
import com.market.ecommerce.domain.order.mapper.OrderMapper;
import com.market.ecommerce.domain.order.repository.OrderRepository;
import com.market.ecommerce.domain.user.entity.impl.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final RedisClient redisClient;

    private static final String ORDER_LAST_UNIQUE_ID_SUFFIX_KEY_PREFIX = "order:suffix:";
    private static final long TTL_SECONDS = 24 * 60 * 60 + 60;

    @Transactional
    public Order createOrder(OrderCreate.Request req, Customer customer) {
        String orderId = generateOrderId();

        List<OrderItem> orderItems = req.getProductInfos().stream()
                .map(this::toOrderItem)
                .toList();

        int totalAmount = orderItems.stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum();

        Order order = orderMapper.toEntity(orderId, customer, totalAmount, orderItems);

        return orderRepository.save(order);
    }

    private OrderItem toOrderItem(OrderCreate.Request.ProductInfo productInfo) {
        return OrderItem.builder()
                .productId(productInfo.getProductId())
                .quantity(productInfo.getQuantity())
                .price(productInfo.getPrice())
                .build();
    }

    private String generateOrderId() {
        String date = getCurrentDate();
        String redisKey = buildRedisKey(date);
        Long suffix = redisClient.incrementValueWithExpireOnce(redisKey, TTL_SECONDS);

        return String.format("%s-%06d", date, suffix);
    }

    private String getCurrentDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    private String buildRedisKey(String date) {
        return ORDER_LAST_UNIQUE_ID_SUFFIX_KEY_PREFIX + date;
    }
}
