package com.market.ecommerce.domain.product.mapper;

import com.market.ecommerce.domain.category.entity.Category;
import com.market.ecommerce.domain.product.dto.ProductRegister;
import com.market.ecommerce.domain.product.entity.Product;
import com.market.ecommerce.domain.product.type.ProductStatus;
import com.market.ecommerce.domain.user.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductMapper {
    public static Product toEntity(ProductRegister.Request dto, User user, Category category,  ProductStatus status){
        return Product.builder()
                .user(user)
                .category(category)
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .status(status)
                .createdAt(LocalDateTime.now().withNano(0))
                .updatedAt(LocalDateTime.now().withNano(0))
                .build();
    }
}
