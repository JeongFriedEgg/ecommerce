package com.market.ecommerce.payment.controller;

import com.market.ecommerce.payment.controller.request.CancelPaymentRequest;
import com.market.ecommerce.payment.service.PaymentService;
import com.market.ecommerce.payment.service.response.TossPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/payment")
public class InternalController {
    private final PaymentService paymentService;

    @PostMapping("/cancel")
    public Mono<ResponseEntity<TossPaymentResponse>> cancelPayment(@RequestBody CancelPaymentRequest req) {
        return paymentService.cancelPayment(req.getPaymentKey(), req.getCancelReason())
                .map(ResponseEntity::ok);
    }
}
