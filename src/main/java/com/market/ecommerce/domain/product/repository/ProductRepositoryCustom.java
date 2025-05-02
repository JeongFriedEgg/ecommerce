package com.market.ecommerce.domain.product.repository;

import com.market.ecommerce.domain.product.entity.Product;
import com.market.ecommerce.domain.product.dto.ProductConditionSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> search(ProductConditionSearch.Request condition, Pageable pageable);
}
