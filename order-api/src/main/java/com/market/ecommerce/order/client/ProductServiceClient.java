package com.market.ecommerce.order.client;

import com.market.ecommerce.order.client.dto.ProductStockAdjustmentCommand;
import com.market.ecommerce.order.client.dto.ProductValidationCommand;
import com.market.ecommerce.order.client.dto.ProductValidationResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service", url = "${product-service.url}")
public interface ProductServiceClient {
    @GetMapping("/internal/product/validate/products")
    ProductValidationResult validateProducts(@RequestBody ProductValidationCommand command);

    @PostMapping("/internal/product/stock")
    void increaseStockForCanceledOrder(@RequestBody List<ProductStockAdjustmentCommand> stockAdjustments);
}
