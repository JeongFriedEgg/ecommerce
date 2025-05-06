package com.market.ecommerce.domain.user.service;

import com.market.ecommerce.domain.user.dto.SignUp;
import com.market.ecommerce.domain.user.entity.impl.Admin;
import com.market.ecommerce.domain.user.exception.UserException;
import com.market.ecommerce.domain.user.mapper.SignUpMapper;
import com.market.ecommerce.domain.user.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.market.ecommerce.domain.user.exception.UserErrorCode.*;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final SignUpMapper signUpMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SignUp.Response signUp(SignUp.Request request) {
        SignUp.AdminRequest req = (SignUp.AdminRequest) request;
        String username = req.getId();
        String email = req.getEmail();
        String phoneNumber = req.getPhoneNumber();

        Optional<Admin> existingAdmin = adminRepository
                .findByUsernameOrEmailOrPhoneNumber(username,email,phoneNumber);

        if (existingAdmin.isPresent()) {
            Admin admin = existingAdmin.get();
            if (admin.getUsername().equals(username)) {
                throw new UserException(USERNAME_ALREADY_EXISTS);
            }
            if (admin.getEmail().equals(email)) {
                throw new UserException(EMAIL_ALREADY_EXISTS);
            }
            if (admin.getPhoneNumber().equals(phoneNumber)) {
                throw new UserException(PHONE_NUMBER_ALREADY_EXISTS);
            }
        }

        Admin admin = signUpMapper.toAdminEntity(req, bCryptPasswordEncoder);
        adminRepository.save(admin);

        return SignUp.Response.fromAdminEntity(admin);
    }
}
