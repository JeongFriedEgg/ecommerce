package com.market.ecommerce.domain.category.service;

import com.market.ecommerce.domain.category.entity.Category;
import com.market.ecommerce.domain.category.exception.CategoryException;
import com.market.ecommerce.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.market.ecommerce.domain.category.exception.CategoryErrorCode.CATEGORY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> validateCategoryNamesExist(List<String> categoryNames) {
        List<Category> categories = categoryRepository.findByNameIn(categoryNames);

        List<String> foundNames = categories.stream()
                .map(Category::getName)
                .toList();

        List<String> notFound = categoryNames.stream()
                .filter(name -> !foundNames.contains(name))
                .toList();

        if (!notFound.isEmpty()) {
            throw new CategoryException(CATEGORY_NOT_FOUND);
        }

        return categories;
    }

}
