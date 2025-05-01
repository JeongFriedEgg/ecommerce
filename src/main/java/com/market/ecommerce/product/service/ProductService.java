package com.market.ecommerce.product.service;

import com.market.ecommerce.exception.product.ProductException;
import com.market.ecommerce.category.domain.Category;
import com.market.ecommerce.product.domain.Product;
import com.market.ecommerce.product.dto.*;
import com.market.ecommerce.product.repository.ProductRepository;
import com.market.ecommerce.product.type.ProductStatus;
import com.market.ecommerce.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.market.ecommerce.exception.product.ProductErrorCode.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product registerProduct(User user, Category category, ProductRegister.Request req) {
        ProductStatus status = parseProductStatus(req.getStatus());
        Product product = Product.create(user, category, req, status);
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(User user, Category category, ProductUpdate.Request req) {
        Product product = findProductOrThrow(req.getProductId());
        validateOwnership(product, user);
        ProductStatus status = parseProductStatus(req.getStatus());
        product.update(req, category, status);
        return product;
    }

    @Transactional
    public void deleteProduct(User user, ProductDelete req) {
        Product product = findProductOrThrow(req.getProductId());
        validateOwnership(product, user);
        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public ProductDetail.Response getProductDetail(Long productId){
        Product product = findProductOrThrow(productId);
        return ProductDetail.Response.from(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductConditionSearch.Response> getProductList(
            ProductConditionSearch.Request req, Pageable pageable) {
        return productRepository.search(req, pageable)
                .map(ProductConditionSearch.Response::from);
    }

    @Transactional(readOnly = true)
    public StockGet.Response getStock(StockGet.Request req) {
        Product product = findProductOrThrow(req.getProductId());
        return StockGet.Response.from(product);
    }

    @Transactional
    public StockUpdate.Response increaseStock(StockUpdate.Request req){
        Product product = findProductOrThrow(req.getProductId());
        product.setStock(product.getStock() + req.getQuantity());
        return StockUpdate.Response.from(product);
    }

    @Transactional
    public StockUpdate.Response decreaseStock(StockUpdate.Request req){
        Product product = findProductOrThrow(req.getProductId());
        int newStock = product.getStock() - req.getQuantity();
        if (newStock < 0){
            throw new ProductException(STOCK_UNDERFLOW);
        }
        product.setStock(newStock);
        return StockUpdate.Response.from(product);
    }

    private ProductStatus parseProductStatus(String statusStr) {
        try {
            return ProductStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new ProductException(INVALID_PRODUCT_STATUS);
        }
    }

    private Product findProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));
    }

    private void validateOwnership(Product product, User user) {
        if (!product.getUser().getId().equals(user.getId())) {
            throw new ProductException(PRODUCT_ACCESS_DENIED);
        }
    }
}
