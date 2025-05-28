package com.market.ecommerce.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", url = "${order-service.url}")
public interface OrderServiceClient {
    @GetMapping("/internal/order/confirm")
    void updateOrderStateWhenConfirmPayment(@PathVariable("orderId") String orderId);
}
