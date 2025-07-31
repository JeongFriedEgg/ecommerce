package com.market.ecommerce.order.client.dto;

import com.market.ecommerce.type.product.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProductValidationCommand {
    private List<ProductInfo> productInfos;

    @Getter
    @Builder
    public static class ProductInfo {
        private Long productId;
        private Integer price;
        private Integer quantity;
        private ProductType status;
    }
}