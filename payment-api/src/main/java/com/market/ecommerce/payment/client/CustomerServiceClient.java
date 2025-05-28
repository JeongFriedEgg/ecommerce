package com.market.ecommerce.payment.client;

import com.market.ecommerce.user.entity.impl.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user-service.url}")
public interface CustomerServiceClient {
    @GetMapping("/internal/customer/{username}")
    Customer getCustomerByUsername(@PathVariable("username")String username);
}
