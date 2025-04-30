package com.market.ecommerce.product.service;

import com.market.ecommerce.exception.product.ProductException;
import com.market.ecommerce.exception.user.UserException;
import com.market.ecommerce.product.domain.Category;
import com.market.ecommerce.product.domain.Product;
import com.market.ecommerce.product.dto.ProductRegister;
import com.market.ecommerce.product.repository.CategoryRepository;
import com.market.ecommerce.product.repository.ProductRepository;
import com.market.ecommerce.product.type.ProductStatus;
import com.market.ecommerce.user.domain.User;
import com.market.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.market.ecommerce.exception.product.ProductErrorCode.*;
import static com.market.ecommerce.exception.user.UserErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProductRegister.Response registerProduct(ProductRegister.Request req) {
        User user = userRepository.findByUserId(req.getUserId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Category category = categoryRepository.findById(Long.parseLong(req.getCategory()))
                .orElseThrow(() -> new ProductException(INVALID_CATEGORY));

        ProductStatus status = parseProductStatus(req.getStatus());

        Product product = Product.create(user, category, req, status);

        productRepository.save(product);

        return ProductRegister.Response.from(product);
    }

    private ProductStatus parseProductStatus(String statusStr) {
        try {
            return ProductStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new ProductException(INVALID_PRODUCT_STATUS);
        }
    }
}
