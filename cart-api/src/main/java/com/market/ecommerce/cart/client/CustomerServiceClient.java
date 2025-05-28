package com.market.ecommerce.cart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user-service.url}")
public interface CustomerServiceClient {
    @GetMapping("/internal/customer/check/{username}")
    void findCustomerByUsername(@PathVariable("username") String username);
}
