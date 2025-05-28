package com.market.ecommerce.cart.client;

import com.market.ecommerce.cart.client.dto.ProductValidationCommand;
import com.market.ecommerce.cart.client.dto.ProductValidationResult;
import com.market.ecommerce.product.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service", url = "${product-service.url}")
public interface ProductServiceClient {
    @GetMapping("/internal/product/validate/{productId}")
    Product validateProductExists(@PathVariable("productId") Long productId);

    @GetMapping("/internal/availability")
    void validateProductAvailability(@RequestBody Product product);

    @GetMapping("/internal/product/stock/{productId}")
    Integer getStock(@PathVariable Long productId);

    @GetMapping("/internal/product/validate/products")
    ProductValidationResult validateProducts(@RequestBody ProductValidationCommand command);
}
