package com.market.ecommerce.domain.user.controller;

import com.market.ecommerce.domain.user.dto.SignUp;
import com.market.ecommerce.domain.user.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/signup")
    public ResponseEntity<SignUp.Response> signUp(
            @RequestBody SignUp.AdminRequest req) {
        SignUp.Response res = adminService.signUp(req);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }
}
