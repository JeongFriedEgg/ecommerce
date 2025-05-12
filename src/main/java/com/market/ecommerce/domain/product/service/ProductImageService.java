package com.market.ecommerce.domain.product.service;

import com.market.ecommerce.domain.product.dto.ProductRegister;
import com.market.ecommerce.domain.product.entity.Product;
import com.market.ecommerce.domain.product.entity.ProductImage;
import com.market.ecommerce.domain.product.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ProductImageRepository productImageRepository;

    public void saveImageUrls(Product product, List<ProductRegister.ImageOrderInfo> imageOrderInfos, List<String> imageUrls) {
        Map<String, Integer> orderMap = new HashMap<>();
        for (ProductRegister.ImageOrderInfo imageOrderInfo : imageOrderInfos) {
            orderMap.put(imageOrderInfo.getImageFileName(), imageOrderInfo.getOrder());
        }

        List<ProductImage> productImages = new ArrayList<>();
        for (String imageUrl : imageUrls) {
            String[] parts = imageUrl.split("/");
            String fileNameWithExtension = parts[parts.length - 1];
            String fileName = fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf("."))
                    .trim().toLowerCase();

            Integer order = orderMap.get(fileName);

            ProductImage productImage = ProductImage.builder()
                    .product(product)
                    .imageOrder(order)
                    .imageUrl(imageUrl)
                    .build();
            productImages.add(productImage);
        }

        productImageRepository.saveAll(productImages);
    }
}
