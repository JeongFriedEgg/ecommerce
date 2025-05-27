package com.market.ecommerce.domain.product.service;

import com.market.ecommerce.domain.category.entity.Category;
import com.market.ecommerce.domain.product.dto.ProductRegister;
import com.market.ecommerce.domain.product.dto.ProductUpdate;
import com.market.ecommerce.domain.product.entity.Product;
import com.market.ecommerce.domain.product.exception.ProductException;
import com.market.ecommerce.domain.product.mapper.ProductMapper;
import com.market.ecommerce.domain.product.repository.ProductRepository;
import com.market.ecommerce.domain.product.service.dto.ProductStockAdjustmentCommand;
import com.market.ecommerce.domain.user.entity.impl.Seller;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.market.ecommerce.domain.product.exception.ProductErrorCode.*;
import static com.market.ecommerce.domain.product.type.ProductType.AVAILABLE;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public Product register(ProductRegister.Request req, Seller seller, Set<Category> categories){
        Product product = productMapper.toEntity(req, seller, categories);
        return productRepository.save(product);
    }

    @Transactional
    public Product update(ProductUpdate.Request req, Seller seller, Set<Category> categories) {
        Product product = productRepository.findById(req.getId())
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        if (!product.getSellerId().equals(seller)) {
            throw new ProductException(UNAUTHORIZED_PRODUCT_ACCESS);
        }

        product.updateInfo(categories, req.getTitle(), req.getDescription(), req.getPrice(), req.getStock());

        try {
            return product;
        } catch (OptimisticLockException e) {
            throw new ProductException(OPTIMISTIC_LOCK_FAILURE);
        }
    }

    @Transactional
    public void increaseStockForCanceledOrder(List<ProductStockAdjustmentCommand> stockAdjustments) {
        for (ProductStockAdjustmentCommand adjustment : stockAdjustments) {
            Product product = productRepository.findById(adjustment.getProductId())
                    .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

            product.increaseStock(adjustment.getQuantity());

            try {
                productRepository.save(product);
            } catch (OptimisticLockException e) {
                throw new ProductException(OPTIMISTIC_LOCK_FAILURE);
            }
        }
    }

    @Transactional
    public void decreaseStockForOrder(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        if (product.getStock() < quantity) {
            throw new ProductException(PRODUCT_INSUFFICIENT_STOCK);
        }

        product.decreaseStock(quantity);

        try {
            productRepository.save(product);
        } catch (OptimisticLockException e) {
            throw new ProductException(OPTIMISTIC_LOCK_FAILURE);
        }
    }

    public Product findProductById(Long productId, Seller seller) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        if (!product.getSellerId().equals(seller)) {
            throw new ProductException(UNAUTHORIZED_PRODUCT_ACCESS);
        }

        return product;
    }

    public Product validateProductExists(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));
    }

    public void validateProductAvailability(Product product) {
        if (!product.getStatus().equals(AVAILABLE)) {
            throw new ProductException(PRODUCT_NOT_AVAILABLE);
        }
    }

    public int getStock(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));
        return product.getStock();
    }

    @Transactional
    public void delete(Product product) {
        productRepository.delete(product);
    }

    public List<Product> findAllById(Set<Long> ids) {
        return productRepository.findAllById(ids);
    }
}
