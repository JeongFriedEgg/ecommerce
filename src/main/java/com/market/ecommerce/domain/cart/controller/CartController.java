package com.market.ecommerce.domain.cart.controller;

import com.market.ecommerce.domain.cart.application.CartApplication;
import com.market.ecommerce.domain.cart.dto.AddProductToCart;
import com.market.ecommerce.domain.cart.dto.RemoveProductFromCart;
import com.market.ecommerce.domain.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartApplication cartApplication;

    @PostMapping
    public ResponseEntity<AddProductToCart.Response> addProductToCart(
            @RequestBody AddProductToCart.Request req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        String username = userDetails.getUsername();
        AddProductToCart.Response res =
                cartApplication.addProductToCart(req, username);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }

    @DeleteMapping("/")
    public ResponseEntity<Void> deleteProductFromCart(
            @RequestBody RemoveProductFromCart.Request req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        String username = userDetails.getUsername();
        cartApplication.removeProductFromCart(req, username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        String username = userDetails.getUsername();
        cartApplication.clearCart(username);
        return ResponseEntity.noContent().build();
    }
}
