package com.market.ecommerce.domain.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.market.ecommerce.domain.payment.service.response.TossPaymentResponse;
import lombok.Builder;
import lombok.Getter;

public class PaymentConfirm {
    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Request {
        private String orderId;
        private String amount;
        private String paymentKey;
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Response {
        private String orderId;
        private String orderName;
        private Long totalAmount;
        private String approvedAt;

        public static Response from(TossPaymentResponse tossPaymentResponse) {
            return Response.builder()
                    .orderId(tossPaymentResponse.getOrderId())
                    .orderName(tossPaymentResponse.getOrderName())
                    .totalAmount(tossPaymentResponse.getTotalAmount())
                    .approvedAt(tossPaymentResponse.getApprovedAt())
                    .build();
        }
    }
}
