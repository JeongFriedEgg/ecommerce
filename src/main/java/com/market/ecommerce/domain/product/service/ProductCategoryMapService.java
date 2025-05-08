package com.market.ecommerce.domain.product.service;

import com.market.ecommerce.domain.category.entity.Category;
import com.market.ecommerce.domain.product.entity.Product;
import com.market.ecommerce.domain.product.entity.ProductCategoryMap;
import com.market.ecommerce.domain.product.repository.ProductCategoryMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCategoryMapService {

    private final ProductCategoryMapRepository productCategoryMapRepository;

    public void mapCategoriesToProduct(Product product, List<Category> categories) {
        List<ProductCategoryMap> mappings = categories.stream()
                .map(category -> ProductCategoryMap.builder()
                        .productId(product)
                        .categoryId(category)
                        .name(category.getName())
                        .build())
                .toList();
        productCategoryMapRepository.saveAll(mappings);
    }

    public void remapCategoriesToProduct(Product product, List<Category> categories) {
        productCategoryMapRepository.deleteByProductId(product);

        mapCategoriesToProduct(product, categories);
    }
}
