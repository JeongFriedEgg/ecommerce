package com.market.ecommerce.domain.product.entity;

import com.market.ecommerce.domain.category.entity.Category;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "product_category_map")
@Getter
@Setter
@Builder
@EntityListeners(value = {AuditingEntityListener.class})
public class ProductCategoryMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product productId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category categoryId;

    private String name;
}
