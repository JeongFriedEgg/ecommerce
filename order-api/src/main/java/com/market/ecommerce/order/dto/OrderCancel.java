package com.market.ecommerce.order.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.market.ecommerce.order.client.dto.TossPaymentResponse;
import com.market.ecommerce.order.entity.Order;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class OrderCancel {
    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Request {
        private String orderId;
        private String paymentKey;
        private Long amount;
        private String cancelReason;
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Response {
        private String orderId;
        private String orderName;
        private Long cancelAmount;
        private LocalDateTime canceledAt;

        /**
         * 더 있을까?
         */

        public static Response from(TossPaymentResponse tossPaymentResponse, Order order) {
            return Response.builder()
                    .orderId(order.getOrderId())
                    .orderName(tossPaymentResponse.getOrderName())
                    .cancelAmount(Long.valueOf(order.getTotalAmount()))
                    .canceledAt(order.getOrderCanceledDate())
                    .build();
        }
    }
}
