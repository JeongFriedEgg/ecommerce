package com.market.ecommerce.domain.product.repository;

import com.market.ecommerce.domain.product.entity.Product;
import com.market.ecommerce.domain.product.entity.ProductCategoryMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryMapRepository extends JpaRepository<ProductCategoryMap, Long> {

    void deleteByProductId(Product product);
}
