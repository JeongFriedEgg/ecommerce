package com.market.ecommerce.order.client;

import com.market.ecommerce.order.client.dto.CancelPaymentRequest;
import com.market.ecommerce.order.client.dto.TossPaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", url = "${payment-service.url}")
public interface PaymentServiceClient {
    @PostMapping("/internal/payment/cancel")
    TossPaymentResponse cancelPayment(@RequestBody CancelPaymentRequest request);
}
