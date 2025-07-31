package com.market.ecommerce.order.controller;

import com.market.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/order")
public class InternalController {

    private final OrderService orderService;

    @GetMapping("/confirm")
    public ResponseEntity<Void> updateOrderStateWhenConfirmPayment(@PathVariable("orderId") String orderId) {
        orderService.updateOrderStateWhenConfirmPayment(orderId);
        return ResponseEntity.ok(null);
    }
}
