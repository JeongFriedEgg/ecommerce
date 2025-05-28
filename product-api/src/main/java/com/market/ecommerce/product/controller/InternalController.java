package com.market.ecommerce.product.controller;

import com.market.ecommerce.product.entity.Product;
import com.market.ecommerce.product.service.ProductService;
import com.market.ecommerce.product.service.ProductValidationService;
import com.market.ecommerce.product.service.dto.ProductStockAdjustmentCommand;
import com.market.ecommerce.product.service.dto.ProductValidationCommand;
import com.market.ecommerce.product.service.dto.ProductValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/product")
public class InternalController {

    private final ProductService productService;
    private final ProductValidationService productValidationService;

    @GetMapping("/validate")
    public ResponseEntity<Product> validateProductExists(@PathVariable Long productId) {
        Product product = productService.validateProductExists(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/availability")
    public ResponseEntity<Void> validateProductAvailability(@RequestBody Product product) {
        productService.validateProductAvailability(product);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/stock")
    public ResponseEntity<Integer> getStock(@PathVariable Long productId) {
        int stock = productService.getStock(productId);
        return ResponseEntity.ok(stock);
    }

    @GetMapping("/validate/products")
    public ResponseEntity<ProductValidationResult> validateProducts(@RequestBody ProductValidationCommand command) {
        ProductValidationResult productValidationResult = productValidationService.validateProducts(command);
        return ResponseEntity.ok(productValidationResult);
    }

    @PostMapping("/stock")
    public ResponseEntity<Void> increaseStockForCanceledOrder(@RequestBody List<ProductStockAdjustmentCommand> stockAdjustments) {
        productService.increaseStockForCanceledOrder(stockAdjustments);
        return ResponseEntity.ok(null);
    }
}
