package com.market.ecommerce.payment.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TossPaymentConfirmRequest {
    private String paymentKey;
    private String orderId;
    private Long amount;
}
