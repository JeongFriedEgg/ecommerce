package com.market.ecommerce.domain.user.controller;

import com.market.ecommerce.domain.user.application.SignUpApplication;
import com.market.ecommerce.domain.user.dto.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final SignUpApplication signUpApplication;

    @PostMapping("/signup")
    public ResponseEntity<SignUp.Response> signUp(
            @RequestBody SignUp.CustomerRequest req) {
        SignUp.Response res = signUpApplication.signUpCustomer(req);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }
}
