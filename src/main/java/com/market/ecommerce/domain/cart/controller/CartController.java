package com.market.ecommerce.domain.cart.controller;

import com.market.ecommerce.domain.cart.application.CartApplication;
import com.market.ecommerce.domain.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartApplication cartApplication;

    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        String username = userDetails.getUsername();
        cartApplication.clearCart(username);
        return ResponseEntity.noContent().build();
    }
}
