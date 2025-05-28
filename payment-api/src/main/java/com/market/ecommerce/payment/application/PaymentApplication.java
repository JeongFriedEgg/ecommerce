package com.market.ecommerce.payment.application;

import com.market.ecommerce.exception.domain.order.OrderException;
import com.market.ecommerce.exception.domain.payment.TossConfirmPaymentException;
import com.market.ecommerce.exception.domain.payment.TossGetPaymentException;
import com.market.ecommerce.exception.domain.payment.TossPaymentException;
import com.market.ecommerce.payment.client.CustomerServiceClient;
import com.market.ecommerce.payment.client.OrderServiceClient;
import com.market.ecommerce.payment.dto.PaymentConfirm;
import com.market.ecommerce.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaymentApplication {

    private final CustomerServiceClient customerServiceClient;
    private final PaymentService paymentService;
    private final OrderServiceClient orderServiceClient;

    @Transactional(
            rollbackFor = {
                    TossGetPaymentException.class, TossConfirmPaymentException.class,
                    TossPaymentException.class, OrderException.class}
    )
    public Mono<PaymentConfirm.Response> confirmPayment(
            PaymentConfirm.Request req, String customerId
    ){

        /*
        *   1. 요청받은 값(paymentKey, orderId, amount)이 PG(토스페이먼츠)사에서 생성된
        *      결제 생성 데이터와 일치하는지 확인
        *   2. 결제 승인 요청을 서버에서 토스페이먼츠 서버로 전송
        * */

        return customerServiceClient.getCustomerByUsername(customerId)
                .flatMap(customer ->
                        paymentService.getPayment(req)
                )
                .then(Mono.defer(() ->
                        paymentService.confirmPayment(
                                req.getPaymentKey(), req.getOrderId(), Long.valueOf(req.getAmount()))
                ))
                .flatMap(paymentConfirm ->
                        orderServiceClient.updateOrderStateWhenConfirmPayment(req.getOrderId())
                                .thenReturn(paymentConfirm)
                )
                .map(PaymentConfirm.Response::from)
                .onErrorResume(Mono::error);
    }
}
