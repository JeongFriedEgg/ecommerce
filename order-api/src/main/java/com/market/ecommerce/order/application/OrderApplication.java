package com.market.ecommerce.order.application;

import com.market.ecommerce.order.client.CustomerServiceClient;
import com.market.ecommerce.order.client.PaymentServiceClient;
import com.market.ecommerce.order.client.ProductServiceClient;
import com.market.ecommerce.order.client.dto.CancelPaymentRequest;
import com.market.ecommerce.order.client.dto.ProductStockAdjustmentCommand;
import com.market.ecommerce.order.client.dto.ProductValidationCommand;
import com.market.ecommerce.order.client.dto.TossPaymentResponse;
import com.market.ecommerce.order.dto.OrderCancel;
import com.market.ecommerce.order.dto.OrderCreate;
import com.market.ecommerce.order.entity.Order;
import com.market.ecommerce.order.mapper.OrderMapper;
import com.market.ecommerce.order.service.OrderService;
import com.market.ecommerce.user.entity.impl.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderApplication {
    private final OrderService orderService;
    private final CustomerServiceClient customerServiceClient;
    private final ProductServiceClient productServiceClient;
    private final PaymentServiceClient paymentServiceClient;
    private final OrderMapper orderMapper;

    public OrderCreate.Response createOrder(
            OrderCreate.Request req, String customerId
    ){
        Customer customer = customerServiceClient.getCustomerByUsername(customerId);

        /* 주문정보에 대한 검증
                  1. 상품 존재 여부
                  2. 상품의 가격 일치 여부
                  3. 재고의 충분성 여부
                  4. 판매 가능 여부
            위의 문제가 발생한 경우, 변경된 부분에 대해 메세지와 상품 객체 데이터들을 담아 반환.
            상품 데이터들은 유효한 상품(존재하는 상품)에 대해서만 반환
        */
        ProductValidationCommand command = orderMapper.toProductValidationCommand(req.getProductInfos());
        productServiceClient.validateProducts(command);

        Order createdOrder = orderService.createOrder(req, customer);

        return OrderCreate.Response.from(createdOrder);
    }

    public OrderCancel.Response cancelOrder(
            OrderCancel.Request req, String customerId
    ){
        customerServiceClient.getCustomerByUsername(customerId);

        /*
         *      1. orderId, paymentKey, amount 값들의 유효성 검증.
         *      2. 결제 취소 요청
         *      3. 주문 데이터 취소 업데이트
         *      4. 상품의 재고 변경
         * */
        TossPaymentResponse paymentCancel=
                paymentServiceClient.cancelPayment(new CancelPaymentRequest(req.getPaymentKey(), req.getCancelReason()));

        Order cancelOrder = orderService.cancelOrder(req);

        List<ProductStockAdjustmentCommand> stockAdjustments = orderMapper.toProductStockAdjustments(cancelOrder.getOrderItems());

        if (!stockAdjustments.isEmpty()) {
            productServiceClient.increaseStockForCanceledOrder(stockAdjustments);
        }

        return OrderCancel.Response.from(paymentCancel, cancelOrder);
    }
}
