package com.market.ecommerce.product.mapper;

import com.market.ecommerce.category.entity.Category;
import com.market.ecommerce.product.dto.ProductRegister;
import com.market.ecommerce.product.entity.Product;
import com.market.ecommerce.type.product.ProductType;
import com.market.ecommerce.user.entity.impl.Seller;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ProductMapper {

    public Product toEntity(ProductRegister.Request req, Seller seller, Set<Category> categories) {
        return Product.builder()
                .sellerId(seller)
                .categories(categories)
                .title(req.getTitle())
                .description(req.getDescription())
                .price(req.getPrice())
                .stock(req.getStock())
                .status(ProductType.AVAILABLE)
                .build();
    }
}
