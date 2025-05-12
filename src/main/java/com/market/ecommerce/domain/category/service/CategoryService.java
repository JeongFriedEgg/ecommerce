package com.market.ecommerce.domain.category.service;

import com.market.ecommerce.domain.category.entity.Category;
import com.market.ecommerce.domain.category.exception.CategoryException;
import com.market.ecommerce.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.market.ecommerce.domain.category.exception.CategoryErrorCode.CATEGORY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Cacheable(value = "categoryCache", key = "'all'")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Set<Category> validateCategoriesExist(Set<String> categoryNames) {
        List<Category> allCategories = getAllCategories();

        Map<String, Category> categoryMap = allCategories.stream()
                .collect(Collectors.toMap(Category::getName, Function.identity()));

        Set<Category> categories = categoryNames.stream()
                                .map(categoryMap::get)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet());

        if (categories.size() != categoryNames.size()) {
            throw new CategoryException(CATEGORY_NOT_FOUND);
        }

        return categories;
    }

}
