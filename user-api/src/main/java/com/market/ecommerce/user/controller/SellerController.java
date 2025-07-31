package com.market.ecommerce.user.controller;

import com.market.ecommerce.user.application.SignUpApplication;
import com.market.ecommerce.user.dto.SignUp;
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

    private final SignUpApplication signUpApplication;

    @PostMapping("/signup")
    public ResponseEntity<SignUp.Response> signUp(
            @RequestBody SignUp.SellerRequest req) {
        SignUp.Response res = signUpApplication.signUpSeller(req);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }
}
