package com.market.ecommerce.domain.product.application;

import com.market.ecommerce.domain.category.entity.Category;
import com.market.ecommerce.domain.category.exception.CategoryException;
import com.market.ecommerce.domain.category.service.CategoryService;
import com.market.ecommerce.domain.product.dto.RegisterProduct;
import com.market.ecommerce.domain.product.entity.Product;
import com.market.ecommerce.domain.product.service.ProductCategoryMapService;
import com.market.ecommerce.domain.product.service.ProductService;
import com.market.ecommerce.domain.user.entity.impl.Seller;
import com.market.ecommerce.domain.user.exception.UserException;
import com.market.ecommerce.domain.user.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterProductApplication {

    private final SellerService sellerService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final ProductCategoryMapService productCategoryMapService;

    @Transactional(rollbackFor = {UserException.class, CategoryException.class})
    public RegisterProduct.Response registerProduct(RegisterProduct.Request req, String username){

        Seller seller = sellerService.getSellerById(username);

        List<Category> categories = categoryService.validateCategoryNamesExist(req.getCategories());

        Product product = productService.registerProduct(req, seller);

        productCategoryMapService.saveProductCategoryMappings(product, categories);

        return RegisterProduct.Response.fromProductEntity(product);
    }
}
