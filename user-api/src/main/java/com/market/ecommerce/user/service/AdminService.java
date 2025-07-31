package com.market.ecommerce.user.service;

import com.market.ecommerce.user.entity.impl.Admin;
import com.market.ecommerce.user.repository.AdminRepository;
import com.market.ecommerce.user.dto.SignUp;
import com.market.ecommerce.exception.domain.user.UserException;
import com.market.ecommerce.user.mapper.SignUpMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final SignUpMapper signUpMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void checkUsernameDuplication(String username) {
        Optional<Admin> existing = adminRepository.findByUsername(username);
        if (existing.isPresent()) {
            throw new UserException(USERNAME_ALREADY_EXISTS);
        }
    }

    public void checkEmailOrPhoneNumberDuplication(String email, String phoneNumber) {
        Optional<Admin> existing = adminRepository.findByEmailOrPhoneNumber(email, phoneNumber);
        if (existing.isPresent()) {
            Admin admin = existing.get();
            if (admin.getEmail().equals(email)) {
                throw new UserException(EMAIL_ALREADY_EXISTS);
            }
            if (admin.getPhoneNumber().equals(phoneNumber)) {
                throw new UserException(PHONE_NUMBER_ALREADY_EXISTS);
            }
        }
    }

    public SignUp.Response register(SignUp.AdminRequest req) {
        Admin admin = signUpMapper.toAdminEntity(req, bCryptPasswordEncoder);
        adminRepository.save(admin);

        return SignUp.Response.fromAdminEntity(admin);
    }
}
