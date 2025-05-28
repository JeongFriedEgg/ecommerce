package com.market.ecommerce.order.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.market.ecommerce.order.entity.Order;
import com.market.ecommerce.type.product.ProductType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class OrderCreate {
    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Request {
        private List<ProductInfo> productInfos;

        @Getter
        @Builder
        public static class ProductInfo {
            private Long productId;
            private Integer price;
            private Integer quantity; // 고객이 구매하려는 상품의 개수
            private ProductType status;
        }
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Response {
        private String orderId;
        private Integer amount;

        public static Response from(Order order) {
            return Response.builder()
                    .orderId(order.getOrderId())
                    .amount(order.getTotalAmount())
                    .build();
        }
    }
}
