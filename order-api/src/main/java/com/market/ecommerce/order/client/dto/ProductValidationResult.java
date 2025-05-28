package com.market.ecommerce.order.client.dto;

import com.market.ecommerce.type.product.ProductType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductValidationResult {
    private List<String> messages;
    private List<ProductItem> productItems;

    @Getter
    @Builder
    public static class ProductItem {
        private Long productId;
        private String title;
        private String mainImageUrl;
        private Integer price;
        private Integer quantity;
        private ProductType status;
    }
}
