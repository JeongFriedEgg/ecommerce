package com.market.ecommerce.domain.product.service;

import com.market.ecommerce.domain.product.dto.ProductDelete;
import com.market.ecommerce.domain.product.dto.ProductRegister;
import com.market.ecommerce.domain.product.dto.ProductUpdate;
import com.market.ecommerce.domain.product.entity.Product;
import com.market.ecommerce.domain.product.exception.ProductException;
import com.market.ecommerce.domain.product.mapper.ProductMapper;
import com.market.ecommerce.domain.product.repository.ProductRepository;
import com.market.ecommerce.domain.user.entity.impl.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.market.ecommerce.domain.product.exception.ProductErrorCode.PRODUCT_NOT_FOUND;
import static com.market.ecommerce.domain.product.exception.ProductErrorCode.UNAUTHORIZED_PRODUCT_ACCESS;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public Product register(ProductRegister.Request req, Seller seller){
        Product product = productMapper.toEntity(req, seller);
        return productRepository.save(product);
    }

    public Product update(ProductUpdate.Request req, Seller seller) {
        Product product = productRepository.findById(req.getId())
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        if (!product.getSellerId().equals(seller)) {
            throw new ProductException(UNAUTHORIZED_PRODUCT_ACCESS);
        }

        product.updateInfo(req.getTitle(), req.getDescription(), req.getPrice(), req.getStock());

        return product;
    }

    public Product findProductById(Long productId, Seller seller) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        if (!product.getSellerId().equals(seller)) {
            throw new ProductException(UNAUTHORIZED_PRODUCT_ACCESS);
        }

        return product;
    }

    public void delete(Product product) {
        productRepository.delete(product);
    }
}
