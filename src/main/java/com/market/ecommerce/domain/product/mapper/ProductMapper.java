package com.market.ecommerce.domain.product.mapper;

import com.market.ecommerce.domain.product.dto.RegisterProduct;
import com.market.ecommerce.domain.product.entity.Product;
import com.market.ecommerce.domain.product.type.ProductType;
import com.market.ecommerce.domain.user.entity.impl.Seller;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(RegisterProduct.Request req, Seller seller) {
        return Product.builder()
                .sellerId(seller)
                .title(req.getTitle())
                .description(req.getDescription())
                .price(req.getPrice())
                .stock(req.getStock())
                .status(ProductType.AVAILABLE)
                .build();
    }
}
