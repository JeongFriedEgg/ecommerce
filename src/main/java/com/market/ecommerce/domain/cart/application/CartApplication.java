package com.market.ecommerce.domain.cart.application;

import com.market.ecommerce.domain.cart.service.CartService;
import com.market.ecommerce.domain.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartApplication {
    private final CustomerService customerService;
    private final CartService cartService;

    public void clearCart(String username) {
        customerService.findCustomerByUsername(username);
        cartService.clearCart(username);
    }
}
