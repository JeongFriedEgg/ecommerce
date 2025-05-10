package com.market.ecommerce.domain.user.service;

import com.market.ecommerce.domain.user.dto.SignUp;
import com.market.ecommerce.domain.user.entity.impl.Seller;
import com.market.ecommerce.domain.user.exception.UserException;
import com.market.ecommerce.domain.user.mapper.SignUpMapper;
import com.market.ecommerce.domain.user.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.market.ecommerce.domain.user.exception.UserErrorCode.*;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final SignUpMapper signUpMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void checkUsernameDuplication(String username) {
        Optional<Seller> existing = sellerRepository.findByUsername(username);
        if (existing.isPresent()) {
            throw new UserException(USERNAME_ALREADY_EXISTS);
        }
    }

    public void checkEmailOrPhoneDuplication(String email, String phoneNumber) {
        Optional<Seller> existing = sellerRepository.findByEmailOrPhoneNumber(email, phoneNumber);
        if (existing.isPresent()) {
            Seller seller = existing.get();
            if (seller.getEmail().equals(email)) {
                throw new UserException(EMAIL_ALREADY_EXISTS);
            }
            if (seller.getPhoneNumber().equals(phoneNumber)) {
                throw new UserException(PHONE_NUMBER_ALREADY_EXISTS);
            }
        }
    }

    public SignUp.Response register(SignUp.SellerRequest req) {
        Seller seller = signUpMapper.toSellerEntity(req, bCryptPasswordEncoder);
        sellerRepository.save(seller);

        return SignUp.Response.fromSellerEntity(seller);
    }

    public Seller findSellerByUsername(String sellerId) {
        return sellerRepository.findByUsername(sellerId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }
}
