package com.market.ecommerce.order.controller;

import com.market.ecommerce.order.application.OrderApplication;
import com.market.ecommerce.order.dto.OrderCancel;
import com.market.ecommerce.order.dto.OrderCreate;
import com.market.ecommerce.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderApplication orderApplication;

    @PostMapping
    public ResponseEntity<OrderCreate.Response> createOrder(
            @RequestBody OrderCreate.Request req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        String username = userDetails.getUsername();

        OrderCreate.Response res =
                orderApplication.createOrder(req, username);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }

    @DeleteMapping
    public ResponseEntity<OrderCancel.Response> cancelOrder(
            @RequestBody OrderCancel.Request req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        String username = userDetails.getUsername();

        OrderCancel.Response res =
                orderApplication.cancelOrder(req,username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }
}
