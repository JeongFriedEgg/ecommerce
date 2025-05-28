package com.market.ecommerce.user.mapper;

import com.market.ecommerce.user.entity.impl.Admin;
import com.market.ecommerce.user.entity.impl.Customer;
import com.market.ecommerce.user.entity.impl.Seller;
import com.market.ecommerce.user.dto.SignUp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SignUpMapper {

    public Customer toCustomerEntity(SignUp.CustomerRequest req, BCryptPasswordEncoder encoder) {
        return Customer.builder()
                .username(req.getId())
                .password(encoder.encode(req.getPassword()))
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .address(req.getAddress())
                .build();
    }

    public Seller toSellerEntity(SignUp.SellerRequest req, BCryptPasswordEncoder encoder) {
        return Seller.builder()
                .username(req.getId())
                .password(encoder.encode(req.getPassword()))
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .accountNumber(req.getAccountNumber())
                .build();
    }

    public Admin toAdminEntity(SignUp.AdminRequest req, BCryptPasswordEncoder encoder) {
        return Admin.builder()
                .username(req.getId())
                .password(encoder.encode(req.getPassword()))
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .build();
    }
}
