package com.market.ecommerce.domain.cart.service.dto;

import com.market.ecommerce.domain.product.type.ProductType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PutCartCommand {
    private Long productId;
    private String title;
    private String mainImageUrl;
    private Integer price;
    private Integer quantity;
    private ProductType status;
}
