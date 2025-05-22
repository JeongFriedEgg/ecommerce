package com.market.ecommerce.domain.payment.application;

import com.market.ecommerce.domain.payment.dto.PaymentConfirm;
import com.market.ecommerce.domain.payment.service.PaymentService;
import com.market.ecommerce.domain.payment.service.request.TossPaymentResponse;
import com.market.ecommerce.domain.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentApplication {

    private final PaymentService paymentService;
    private final CustomerService customerService;

    public PaymentConfirm.Response confirmPayment(
            PaymentConfirm.Request req, String customerId
    ){
        customerService.getCustomerByUsername(customerId);

        /*
        *   1. 요청받은 값(paymentKey, orderId, amount)이 PG(토스페이먼츠)사에서 생성된
        *      결제 생성 데이터와 일치하는지 확인
        *   2. 결제 승인 요청을 서버에서 토스페이먼츠 서버로 전송
        * */
        paymentService.retrievePayment(req);

        TossPaymentResponse paymentConfirm = paymentService.confirmPayment(
                req.getPaymentKey(), req.getOrderId(), Long.valueOf(req.getAmount()));

        return PaymentConfirm.Response.from(paymentConfirm);
    }
}
