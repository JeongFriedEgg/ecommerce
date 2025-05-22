package com.market.ecommerce.domain.order.application;

import com.market.ecommerce.domain.order.dto.OrderCreate;
import com.market.ecommerce.domain.order.entity.Order;
import com.market.ecommerce.domain.order.mapper.OrderMapper;
import com.market.ecommerce.domain.order.service.OrderService;
import com.market.ecommerce.domain.product.service.ProductValidationService;
import com.market.ecommerce.domain.product.service.dto.ProductValidationCommand;
import com.market.ecommerce.domain.user.entity.impl.Customer;
import com.market.ecommerce.domain.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class OrderApplication {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final OrderMapper orderMapper;
    private final ProductValidationService productValidationService;

    public OrderCreate.Response createOrder(
            OrderCreate.Request req, String customerId
    ){
        Customer customer = customerService.getCustomerByUsername(customerId);

        /* 주문정보에 대한 검증
                  1. 상품 존재 여부
                  2. 상품의 가격 일치 여부
                  3. 재고의 충분성 여부
                  4. 판매 가능 여부
            위의 문제가 발생한 경우, 변경된 부분에 대해 메세지와 상품 객체 데이터들을 담아 반환.
            상품 데이터들은 유효한 상품(존재하는 상품)에 대해서만 반환
        */
        ProductValidationCommand command = orderMapper.toProductValidationCommand(req.getProductInfos());
        productValidationService.validateProducts(command);

        Order createdOrder = orderService.createOrder(req, customer);

        return OrderCreate.Response.from(createdOrder);
    }
}
