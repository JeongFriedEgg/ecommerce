package com.market.ecommerce.product.service;

import com.market.ecommerce.exception.product.ProductException;
import com.market.ecommerce.exception.user.UserException;
import com.market.ecommerce.product.domain.Category;
import com.market.ecommerce.product.domain.Product;
import com.market.ecommerce.product.dto.ProductDelete;
import com.market.ecommerce.product.dto.ProductRegister;
import com.market.ecommerce.product.dto.ProductUpdate;
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
        User user = getUserOrThrow(req.getUserId());
        Category category = getCategoryOrThrow(req.getCategory());
        ProductStatus status = parseProductStatus(req.getStatus());

        Product product = Product.create(user, category, req, status);
        productRepository.save(product);

        return ProductRegister.Response.from(product);
    }

    @Transactional
    public ProductUpdate.Response updateProduct(ProductUpdate.Request req) {
        Product product = getProductOrThrow(req.getProductId());
        User user = getUserOrThrow(req.getUserId());
        validateOwnership(product, user);

        Category category = getCategoryOrThrow(req.getCategory());
        ProductStatus status = parseProductStatus(req.getStatus());

        product.update(req, category, status);
        return ProductUpdate.Response.from(product);
    }

    @Transactional
    public void deleteProduct(ProductDelete req) {
        Product product = getProductOrThrow(req.getProductId());
        User user = getUserOrThrow(req.getUserId());
        validateOwnership(product, user);

        productRepository.delete(product);
    }



    private ProductStatus parseProductStatus(String statusStr) {
        try {
            return ProductStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new ProductException(INVALID_PRODUCT_STATUS);
        }
    }

    private User getUserOrThrow(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    private Product getProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));
    }

    private Category getCategoryOrThrow(String categoryIdStr) {
        try {
            Long categoryId = Long.parseLong(categoryIdStr);
            return categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ProductException(INVALID_CATEGORY));
        } catch (NumberFormatException e) {
            throw new ProductException(INVALID_CATEGORY);
        }
    }

    private void validateOwnership(Product product, User user) {
        if (!product.getUser().getId().equals(user.getId())) {
            throw new ProductException(PRODUCT_ACCESS_DENIED);
        }
    }
}
