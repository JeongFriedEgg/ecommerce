package com.market.ecommerce.domain.product.controller;

import com.market.ecommerce.domain.product.application.ProductApplication;
import com.market.ecommerce.domain.product.dto.ProductRegister;
import com.market.ecommerce.domain.product.dto.ProductUpdate;
import com.market.ecommerce.domain.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductApplication productApplication;

    @PostMapping
    public ResponseEntity<ProductRegister.Response> registerProduct(
            @RequestBody ProductRegister.Request req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        String username = userDetails.getUsername();

        ProductRegister.Response res =
                productApplication.registerProduct(req, username);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }

    @PutMapping
    public ResponseEntity<ProductUpdate.Response> updateProduct(
            @RequestBody ProductUpdate.Request req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        String username = userDetails.getUsername();

        ProductUpdate.Response res =
                productApplication.updateProduct(req, username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }
}
