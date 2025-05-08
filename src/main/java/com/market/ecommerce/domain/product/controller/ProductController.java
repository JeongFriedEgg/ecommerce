package com.market.ecommerce.domain.product.controller;

import com.market.ecommerce.domain.product.application.RegisterProductApplication;
import com.market.ecommerce.domain.product.dto.RegisterProduct;
import com.market.ecommerce.domain.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final RegisterProductApplication registerProductApplication;

    @PostMapping
    public ResponseEntity<RegisterProduct.Response> registerProduct(
            @RequestBody RegisterProduct.Request req,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        String username = userDetails.getUsername();

        RegisterProduct.Response res =
                registerProductApplication.registerProduct(req, username);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }
}
