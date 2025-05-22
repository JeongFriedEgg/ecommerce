package com.market.ecommerce.domain.product.service;

import com.market.ecommerce.domain.product.entity.Product;
import com.market.ecommerce.domain.product.entity.ProductImage;
import com.market.ecommerce.domain.product.repository.ProductRepository;
import com.market.ecommerce.domain.product.service.dto.ProductValidationCommand;
import com.market.ecommerce.domain.product.service.dto.ProductValidationResult;
import com.market.ecommerce.domain.product.type.ProductType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductValidationService {
    private final ProductRepository productRepository;

    public ProductValidationResult validateProducts(ProductValidationCommand productValidationCommand) {
        /* 주문정보에 대한 검증
              1. 상품 존재 여부
              2. 상품의 가격 일치 여부
              3. 재고의 충분성 여부
              4. 판매 가능 여부
        */
        List<String> messages = new ArrayList<>();
        List<ProductValidationResult.ProductItem> productItems = new ArrayList<>();

        Map<Long, Product> productMap = productRepository.findAllById(
                productValidationCommand.getProductInfos().stream()
                        .map(ProductValidationCommand.ProductInfo::getProductId)
                        .toList()
        ).stream().collect(Collectors.toMap(Product::getId, p -> p));

        for (ProductValidationCommand.ProductInfo productInfo : productValidationCommand.getProductInfos()) {
            Product product = productMap.get(productInfo.getProductId());
            if (product == null) {
                messages.add(formatMessage("판매자에 의해 삭제된 상품입니다.", productInfo.getProductId()));
                continue;
            }

            validateProductInfo(product, productInfo, messages);

            productItems.add(ProductValidationResult.ProductItem.builder()
                            .productId(product.getId())
                            .title(product.getTitle())
                            .mainImageUrl(product.getProductImages().stream()
                                    .filter(imgUrl ->
                                            imgUrl.getImageOrder() != null && imgUrl.getImageOrder() == 1)
                                    .map(ProductImage::getImageUrl)
                                    .findFirst()
                                    .orElse(null)
                            )
                            .price(product.getPrice())
                            .quantity(productInfo.getQuantity())
                            .status(product.getStatus())
                    .build());
        }

        return ProductValidationResult.builder()
                .messages(messages)
                .productItems(productItems)
                .build();
    }

    private void validateProductInfo(
            Product product, ProductValidationCommand.ProductInfo productInfo, List<String> messages
    ) {
        if (!Objects.equals(product.getPrice(), productInfo.getPrice())) {
            messages.add(formatMessage("가격이 일치하지 않습니다. (요청가: %d, 실제가: %d)",
                    productInfo.getProductId(), productInfo.getPrice(), product.getPrice()));
        }

        if (product.getStock() < productInfo.getQuantity()) {
            messages.add(formatMessage("재고가 부족합니다. (요청 수량: %d, 재고 수량: %d)",
                    productInfo.getProductId(), productInfo.getQuantity(), product.getStock()));
        }

        if (product.getStatus().equals(ProductType.OUT_OF_STOCK)) {
            messages.add(formatMessage("일시 품절 상태입니다.", productInfo.getProductId()));
        } else if (product.getStatus().equals(ProductType.SOLD_OUT)) {
            messages.add(formatMessage("판매 종료되었습니다.", productInfo.getProductId()));
        }
    }

    private String formatMessage(String message, Long productId) {
        return "상품 ID " + productId + " : " + message;
    }

    private String formatMessage(String template, Long productId, Object... args) {
        return "상품 ID " + productId + " : " + String.format(template, args);
    }
}
