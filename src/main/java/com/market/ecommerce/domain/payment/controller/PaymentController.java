package com.market.ecommerce.domain.payment.controller;

import com.market.ecommerce.domain.payment.application.PaymentApplication;
import com.market.ecommerce.domain.payment.dto.PaymentConfirm;
import com.market.ecommerce.domain.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentApplication paymentApplication;

    @PostMapping("/confirm")
    public ResponseEntity<PaymentConfirm.Response> confirmPayment(
            @RequestBody PaymentConfirm.Request req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        String username = userDetails.getUsername();

        PaymentConfirm.Response res =
                paymentApplication.confirmPayment(req, username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }
}
