package com.market.ecommerce.product.controller;

import com.market.ecommerce.product.dto.*;
import com.market.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetail.Response> getProductDetail(
            @PathVariable Long productId
    ){
        ProductDetail.Response res = productService.getProductDetail(productId);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<ProductConditionSearch.Response>> getProductList(
            @RequestBody ProductConditionSearch.Request req,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable
    ){
        Page<ProductConditionSearch.Response> res = productService.getProductList(req, pageable);
        return ResponseEntity.ok(res);
    }
}
