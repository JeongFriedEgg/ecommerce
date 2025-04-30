package com.market.ecommerce.product.controller;

import com.market.ecommerce.product.dto.ProductDelete;
import com.market.ecommerce.product.dto.ProductRegister;
import com.market.ecommerce.product.dto.ProductUpdate;
import com.market.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping
    public ResponseEntity<ProductUpdate.Response> updateProduct(
            @RequestBody ProductUpdate.Request req
    ){
        ProductUpdate.Response res = productService.updateProduct(req);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(
            @RequestBody ProductDelete req
    ){
        productService.deleteProduct(req);
        return ResponseEntity.noContent().build();
    }
}
