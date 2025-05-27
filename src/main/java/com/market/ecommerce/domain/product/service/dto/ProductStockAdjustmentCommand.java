package com.market.ecommerce.domain.product.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductStockAdjustmentCommand {
    private Long productId;
    private Integer quantity;
}
