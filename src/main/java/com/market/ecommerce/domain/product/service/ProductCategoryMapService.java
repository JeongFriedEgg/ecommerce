package com.market.ecommerce.domain.product.service;

import com.market.ecommerce.domain.product.repository.ProductCategoryMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCategoryMapService {

    private final ProductCategoryMapRepository productCategoryMapRepository;
}
