package com.market.ecommerce.product.client;

import com.market.ecommerce.user.entity.impl.Seller;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user-service.url}")
public interface SellerServiceClient {
    @GetMapping("/internal/seller/{username}")
    Seller findSellerByUsername(@PathVariable("username") String username);
}
