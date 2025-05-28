package com.market.ecommerce.payment.controller.request;

import lombok.Getter;

@Getter
public class CancelPaymentRequest {
    private String paymentKey;
    private String cancelReason;
}