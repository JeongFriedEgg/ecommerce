package com.market.ecommerce.payment.client;

import com.market.ecommerce.user.entity.impl.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

@FeignClient(name = "user-service", url = "${user-service.url}")
public interface CustomerServiceClient {
    @GetMapping("/internal/customer/{username}")
    Mono<Customer> getCustomerByUsername(@PathVariable("username")String username);
}
