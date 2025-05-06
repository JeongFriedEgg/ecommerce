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

    public SignUp.Response signUp(SignUp.Request request) {
        SignUp.SellerRequest req = (SignUp.SellerRequest) request;
        String username = req.getId();
        String email = req.getEmail();
        String phoneNumber = req.getPhoneNumber();

        Optional<Seller> existingSeller = sellerRepository
                .findByUsernameOrEmailOrPhoneNumber(username,email,phoneNumber);

        if (existingSeller.isPresent()) {
            Seller seller = existingSeller.get();
            if (seller.getUsername().equals(username)) {
                throw new UserException(USERNAME_ALREADY_EXISTS);
            }
            if (seller.getEmail().equals(email)) {
                throw new UserException(EMAIL_ALREADY_EXISTS);
            }
            if (seller.getPhoneNumber().equals(phoneNumber)) {
                throw new UserException(PHONE_NUMBER_ALREADY_EXISTS);
            }
        }

        Seller seller = signUpMapper.toSellerEntity(req, bCryptPasswordEncoder);
        sellerRepository.save(seller);

        return SignUp.Response.fromSellerEntity(seller);
    }
}
