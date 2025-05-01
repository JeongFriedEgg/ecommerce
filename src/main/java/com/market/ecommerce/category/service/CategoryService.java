package com.market.ecommerce.category.service;

import com.market.ecommerce.category.domain.Category;
import com.market.ecommerce.category.repository.CategoryRepository;
import com.market.ecommerce.exception.product.ProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.market.ecommerce.exception.product.ProductErrorCode.INVALID_CATEGORY;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findCategoryOrThrow(String categoryIdStr) {
        try {
            Long categoryId = Long.parseLong(categoryIdStr);
            return categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ProductException(INVALID_CATEGORY));
        } catch (NumberFormatException e) {
            throw new ProductException(INVALID_CATEGORY);
        }
    }
}
