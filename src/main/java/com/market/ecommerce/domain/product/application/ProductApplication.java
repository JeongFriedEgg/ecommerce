package com.market.ecommerce.domain.product.application;

import com.market.ecommerce.domain.category.entity.Category;
import com.market.ecommerce.domain.category.exception.CategoryException;
import com.market.ecommerce.domain.category.service.CategoryService;
import com.market.ecommerce.domain.product.dto.ProductDelete;
import com.market.ecommerce.domain.product.dto.ProductRegister;
import com.market.ecommerce.domain.product.dto.ProductUpdate;
import com.market.ecommerce.domain.product.entity.Product;
import com.market.ecommerce.domain.product.exception.ProductException;
import com.market.ecommerce.domain.product.service.ProductService;
import com.market.ecommerce.domain.user.entity.impl.Seller;
import com.market.ecommerce.domain.user.exception.UserException;
import com.market.ecommerce.domain.user.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductApplication {

    private final SellerService sellerService;
    private final CategoryService categoryService;
    private final ProductService productService;

    @Transactional(rollbackFor = {UserException.class, CategoryException.class})
    public ProductRegister.Response registerProduct(ProductRegister.Request req, String username){

        Seller seller = sellerService.findSellerByUsername(username);

        Set<Category> categories = categoryService.validateCategoriesExist(req.getCategories());

        Product product = productService.register(req, seller, categories);

        return ProductRegister.Response.fromProductEntity(product);
    }

    @Transactional(rollbackFor = {UserException.class, CategoryException.class, ProductException.class})
    public ProductUpdate.Response updateProduct(ProductUpdate.Request req, String username) {

        Seller seller = sellerService.findSellerByUsername(username);

        Set<Category> categories = categoryService.validateCategoriesExist(req.getCategories());

        Product updatedProduct = productService.update(req, seller, categories);

        return ProductUpdate.Response.fromProductEntity(updatedProduct);
    }

    @Transactional(rollbackFor = {UserException.class, ProductException.class})
    public void deleteProduct(ProductDelete.Request req, String username) {

        Seller seller = sellerService.findSellerByUsername(username);

        Product product = productService.findProductById(req.getId(), seller);

        productService.delete(product);
    }
}
