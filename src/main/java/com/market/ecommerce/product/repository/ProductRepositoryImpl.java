package com.market.ecommerce.product.repository;

import com.market.ecommerce.product.domain.Product;
import com.market.ecommerce.product.domain.QCategory;
import com.market.ecommerce.product.domain.QProduct;
import com.market.ecommerce.product.dto.ProductConditionSearch;
import com.market.ecommerce.product.type.ProductStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> search(ProductConditionSearch.Request condition, Pageable pageable) {
        QProduct product = QProduct.product;
        QCategory category = QCategory.category;

        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(condition.getKeyword())) {
            builder.and(product.name.containsIgnoreCase(condition.getKeyword()));
        }
        if (StringUtils.hasText(condition.getCategory())) {
            builder.and(product.category.name.eq(condition.getCategory()));
        }
        if (StringUtils.hasText(condition.getStatus())) {
            try {
                builder.and(product.status.eq(ProductStatus.valueOf(condition.getStatus().toUpperCase())));
            } catch (IllegalArgumentException ignored) { }
        }

        List<Product> productList = queryFactory
                .selectFrom(product)
                .leftJoin(product.category, category).fetchJoin()
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(product.createdAt.desc())
                .fetch();

        long count = queryFactory
                .select(product.count())
                .from(product)
                .where(builder)
                .fetchOne();
        return new PageImpl<>(productList, pageable, count);
    }
}
