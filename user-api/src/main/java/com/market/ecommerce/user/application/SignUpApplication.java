package com.market.ecommerce.user.application;

import com.market.ecommerce.user.dto.SignUp;
import com.market.ecommerce.user.service.AdminService;
import com.market.ecommerce.user.service.CustomerService;
import com.market.ecommerce.user.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpApplication {
    private final AdminService adminService;
    private final CustomerService customerService;
    private final SellerService sellerService;

    public SignUp.Response signUpAdmin(SignUp.AdminRequest req) {
        checkUsernameDuplication(req.getId());
        adminService.checkEmailOrPhoneNumberDuplication(req.getEmail(), req.getPhoneNumber());
        return adminService.register(req);
    }

    public SignUp.Response signUpCustomer(SignUp.CustomerRequest req) {
        checkUsernameDuplication(req.getId());
        customerService.checkEmailOrPhoneDuplication(req.getEmail(), req.getPhoneNumber());
        return customerService.register(req);
    }

    public SignUp.Response signUpSeller(SignUp.SellerRequest req) {
        checkUsernameDuplication(req.getId());
        sellerService.checkEmailOrPhoneDuplication(req.getEmail(), req.getPhoneNumber());
        return sellerService.register(req);
    }

    private void checkUsernameDuplication(String username) {
        adminService.checkUsernameDuplication(username);
        customerService.checkUsernameDuplication(username);
        sellerService.checkUsernameDuplication(username);
    }
}
