package com.market.ecommerce.product.repository;

import com.market.ecommerce.product.domain.Product;
import com.market.ecommerce.product.dto.ProductConditionSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> search(ProductConditionSearch.Request condition, Pageable pageable);
}
