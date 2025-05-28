package com.market.ecommerce.order.client.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CancelPaymentRequest {
    private String paymentKey;
    private String cancelReason;
}
