package com.market.ecommerce.domain.payment.mapper;

import com.market.ecommerce.domain.payment.entity.TossPayment;
import com.market.ecommerce.domain.payment.service.response.TossPaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class TossPaymentMapper {

    public TossPayment toEntity(TossPaymentResponse res) {
        return TossPayment.builder()
                .paymentKey(res.getPaymentKey())
                .orderId(res.getOrderId())
                .orderName(res.getOrderName())
                .totalAmount(res.getTotalAmount())
                .status(res.getStatus())
                .method(res.getMethod())
                .requestedAt(res.getRequestedAt())
                .approvedAt(res.getApprovedAt())
                .build();
    }
}
