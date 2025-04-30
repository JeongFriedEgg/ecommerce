package com.market.ecommerce.product.controller;

import com.market.ecommerce.product.dto.ProductRegister;
import com.market.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/register")
    public ResponseEntity<ProductRegister.Response> registerProduct(
            @RequestBody ProductRegister.Request req
    ){
        ProductRegister.Response res = productService.registerProduct(req);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }
}
