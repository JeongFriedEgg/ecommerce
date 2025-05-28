package com.market.ecommerce.user.controller;

import com.market.ecommerce.user.entity.impl.Customer;
import com.market.ecommerce.user.entity.impl.Seller;
import com.market.ecommerce.user.service.CustomerService;
import com.market.ecommerce.user.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class InternalController {

    private final CustomerService customerService;
    private final SellerService sellerService;

    @GetMapping("/customer/check/{username}")
    public ResponseEntity<Void> findCustomerByUsername(@PathVariable String username) {
        customerService.findCustomerByUsername(username);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/customer/{username}")
    public ResponseEntity<Customer> getCustomerByUsername(@PathVariable String username) {
        Customer customer = customerService.getCustomerByUsername(username);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/seller/{username}")
    public ResponseEntity<Seller> getSeller(@PathVariable String username) {
        Seller seller = sellerService.findSellerByUsername(username);
        return ResponseEntity.ok(seller);
    }
}
