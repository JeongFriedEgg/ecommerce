package com.market.ecommerce.domain.category.service;

import com.market.ecommerce.domain.category.entity.Category;
import com.market.ecommerce.domain.category.exception.CategoryException;
import com.market.ecommerce.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import static com.market.ecommerce.domain.category.exception.CategoryErrorCode.CATEGORY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Cacheable(value = "categoryCache",
            key = "#categoryNames.stream().sorted().collect(T(java.util.stream.Collectors).joining(','))")
    public Set<Category> validateCategoriesExist(Set<String> categoryNames) {
        Set<Category> categories = categoryRepository.findByNameIn(categoryNames);

        Set<String> foundNames = categories.stream()
                .map(Category::getName)
                .collect(Collectors.toSet());


        if (foundNames.size() != categoryNames.size()) {
            throw new CategoryException(CATEGORY_NOT_FOUND);
        }

        return categories;
    }

}
