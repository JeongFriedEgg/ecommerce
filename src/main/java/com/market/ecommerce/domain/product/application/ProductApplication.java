package com.market.ecommerce.domain.product.application;

import com.market.ecommerce.common.service.FileService;
import com.market.ecommerce.domain.category.entity.Category;
import com.market.ecommerce.domain.category.exception.CategoryException;
import com.market.ecommerce.domain.category.service.CategoryService;
import com.market.ecommerce.domain.product.dto.ProductDelete;
import com.market.ecommerce.domain.product.dto.ProductRegister;
import com.market.ecommerce.domain.product.dto.ProductUpdate;
import com.market.ecommerce.domain.product.entity.Product;
import com.market.ecommerce.domain.product.exception.ProductException;
import com.market.ecommerce.domain.product.service.ProductImageService;
import com.market.ecommerce.domain.product.service.ProductService;
import com.market.ecommerce.domain.user.entity.impl.Seller;
import com.market.ecommerce.domain.user.exception.UserException;
import com.market.ecommerce.domain.user.service.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.market.ecommerce.domain.product.exception.ProductErrorCode.PRODUCT_REGISTRATION_FAILED;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductApplication {

    private final SellerService sellerService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final FileService fileService;
    private final ProductImageService productImageService;

    @Transactional(rollbackFor = {UserException.class, CategoryException.class})
    public ProductRegister.Response registerProduct(
            ProductRegister.Request req, List<MultipartFile> imageFiles, String username
    ){

        Seller seller = sellerService.findSellerByUsername(username);

        Set<Category> categories = categoryService.validateCategoriesExist(req.getCategories());

        List<String> imageUrls = fileService.uploadImages(imageFiles);

        Product product = null;
        try {
            product = productService.register(req, seller, categories);
            productImageService.saveImageUrls(product, req.getImageOrderInfos(), imageUrls);
        } catch (Exception e) {
            fileService.deleteImages(imageUrls);
            log.error("상품 등록 처리 중에 예외가 발생하였습니다. : ",e);
            throw new ProductException(PRODUCT_REGISTRATION_FAILED);
        }

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

        List<String> imageUrls = productImageService.getImageUrlsByProductId(product.getId());

        productService.delete(product);

        fileService.deleteImages(imageUrls);

        /** ProductImage 데이터의 경우, Product 가 삭제될 때, 해당 Product 데이터에 대한
         *  ProductImage 데이터 또한 자동 삭제.
         *  MySQL에서 테이블 생성시 ProductImage의 외래키를 다음과 같이 설정함.
         *
         *  -> FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
         *  */
    }

    private String extractFileNameFromUrl(String url) {
        String s3Path = url.substring(url.lastIndexOf('/') + 1);
        String fileName = s3Path.substring(s3Path.indexOf('_') + 1);
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }
}
