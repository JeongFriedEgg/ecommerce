package com.market.ecommerce.domain.order.mapper;

import com.market.ecommerce.domain.order.dto.OrderCreate;
import com.market.ecommerce.domain.order.entity.Order;
import com.market.ecommerce.domain.order.entity.OrderItem;
import com.market.ecommerce.domain.order.type.OrderState;
import com.market.ecommerce.domain.product.service.dto.ProductStockAdjustmentCommand;
import com.market.ecommerce.domain.product.service.dto.ProductValidationCommand;
import com.market.ecommerce.domain.user.entity.impl.Customer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public Order toEntity(
            String orderId, Customer customer,
            int totalAmount, List<OrderItem> orderItems
    ) {
        return Order.builder()
                .orderId(orderId)
                .customer(customer)
                .orderItems(orderItems)
                .totalAmount(totalAmount)
                .shippingAddress(customer.getAddress())
                .orderState(OrderState.CREATED)
                .build();
    }

    public ProductValidationCommand toProductValidationCommand(List<OrderCreate.Request.ProductInfo> productInfos) {
        List<ProductValidationCommand.ProductInfo> validationProductInfos = productInfos.stream()
                .map(infos -> ProductValidationCommand.ProductInfo.builder()
                        .productId(infos.getProductId())
                        .price(infos.getPrice())
                        .quantity(infos.getQuantity())
                        .status(infos.getStatus())
                        .build()
                ).toList();

        return new ProductValidationCommand(validationProductInfos);
    }

    public List<ProductStockAdjustmentCommand> toProductStockAdjustments(List<OrderItem> orderItems) {
        if (orderItems == null) {
            return List.of();
        }
        return orderItems.stream()
                .map(orderItem -> new ProductStockAdjustmentCommand(orderItem.getProductId(), orderItem.getQuantity()))
                .toList();
    }
}
