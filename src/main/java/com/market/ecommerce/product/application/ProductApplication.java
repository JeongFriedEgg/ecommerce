package com.market.ecommerce.product.application;

import com.market.ecommerce.category.domain.Category;
import com.market.ecommerce.category.service.CategoryService;
import com.market.ecommerce.product.domain.Product;
import com.market.ecommerce.product.dto.ProductDelete;
import com.market.ecommerce.product.dto.ProductRegister;
import com.market.ecommerce.product.dto.ProductUpdate;
import com.market.ecommerce.product.service.ProductService;
import com.market.ecommerce.user.domain.User;
import com.market.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductApplication {

    private final ProductService productService;
    private final UserService userService;
    private final CategoryService categoryService;

    @Transactional
    public ProductRegister.Response registerProduct(ProductRegister.Request req){
        User user = userService.findUserOrThrow(req.getUserId());
        Category category = categoryService.findCategoryOrThrow(req.getCategory());
        Product product = productService.registerProduct(user,category,req);
        return ProductRegister.Response.from(product);
    }

    @Transactional
    public ProductUpdate.Response updateProduct(ProductUpdate.Request req){
        User user = userService.findUserOrThrow(req.getUserId());
        Category category = categoryService.findCategoryOrThrow(req.getCategory());
        Product product = productService.updateProduct(user, category, req);
        return ProductUpdate.Response.from(product);
    }

    @Transactional
    public void deleteProduct(ProductDelete req){
        User user = userService.findUserOrThrow(req.getUserId());
        productService.deleteProduct(user, req);
    }
}
