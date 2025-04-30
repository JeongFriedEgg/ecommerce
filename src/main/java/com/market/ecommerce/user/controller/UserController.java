package com.market.ecommerce.user.controller;

import com.market.ecommerce.user.dto.SignUp;
import com.market.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUp.Response> signUp(
            @RequestBody SignUp.Request req
    ){
        SignUp.Response res = userService.signUp(req);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }
}
