package com.market.ecommerce.domain.user.controller;

import com.market.ecommerce.domain.user.dto.SignUp;
import com.market.ecommerce.domain.user.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

    private final SellerService sellerService;

    @PostMapping("/signup")
    public ResponseEntity<SignUp.Response> signUp(
            @RequestBody SignUp.SellerRequest req) {
        SignUp.Response res = sellerService.signUp(req);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }
}
