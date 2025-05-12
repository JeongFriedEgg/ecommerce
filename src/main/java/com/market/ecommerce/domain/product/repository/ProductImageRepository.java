package com.market.ecommerce.domain.product.repository;

import com.market.ecommerce.domain.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    @Query("SELECT pi.imageUrl FROM ProductImage pi WHERE pi.product.id = :productId")
    List<String> findImageUrlByProductId(Long productId);
}
