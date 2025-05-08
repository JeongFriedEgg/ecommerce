package com.market.ecommerce.domain.product.service;

import com.market.ecommerce.domain.product.dto.ProductRegister;
import com.market.ecommerce.domain.product.entity.Product;
import com.market.ecommerce.domain.product.mapper.ProductMapper;
import com.market.ecommerce.domain.product.repository.ProductRepository;
import com.market.ecommerce.domain.user.entity.impl.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public Product register(ProductRegister.Request req, Seller seller){
        Product product = productMapper.toEntity(req, seller);
        return productRepository.save(product);
    }
}
