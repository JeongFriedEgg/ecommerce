package com.market.ecommerce.product.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.market.ecommerce.category.domain.Category;
import com.market.ecommerce.product.dto.ProductRegister;
import com.market.ecommerce.product.dto.ProductUpdate;
import com.market.ecommerce.product.type.ProductStatus;
import com.market.ecommerce.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "Product")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value = {AuditingEntityListener.class})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ProductStatus status = ProductStatus.AVAILABLE;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static Product create(User user, Category category, ProductRegister.Request req, ProductStatus status) {
        return Product.builder()
                .user(user)
                .category(category)
                .name(req.getName())
                .description(req.getDescription())
                .price(req.getPrice())
                .stock(req.getStock())
                .status(status)
                .createdAt(LocalDateTime.now().withNano(0))
                .updatedAt(LocalDateTime.now().withNano(0))
                .build();
    }

    public void update(ProductUpdate.Request req, Category category, ProductStatus status) {
        this.name = req.getName();
        this.description = req.getDescription();
        this.price = req.getPrice();
        this.stock = req.getStock();
        this.category = category;
        this.status = status;
        this.updatedAt = LocalDateTime.now().withNano(0);
    }
}
