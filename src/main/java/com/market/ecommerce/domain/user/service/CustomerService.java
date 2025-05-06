package com.market.ecommerce.domain.user.service;

import com.market.ecommerce.domain.user.dto.SignUp;
import com.market.ecommerce.domain.user.entity.impl.Customer;
import com.market.ecommerce.domain.user.exception.UserException;
import com.market.ecommerce.domain.user.mapper.SignUpMapper;
import com.market.ecommerce.domain.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.market.ecommerce.domain.user.exception.UserErrorCode.*;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final SignUpMapper signUpMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SignUp.Response signUp(SignUp.Request request) {
        SignUp.CustomerRequest req = (SignUp.CustomerRequest) request;
        String username = req.getId();
        String email = req.getEmail();
        String phoneNumber = req.getPhoneNumber();

        Optional<Customer> existingCustomer = customerRepository
                .findByUsernameOrEmailOrPhoneNumber(username,email,phoneNumber);

        if (existingCustomer.isPresent()) {
            Customer customer = existingCustomer.get();
            if (customer.getUsername().equals(username)) {
                throw new UserException(USERNAME_ALREADY_EXISTS);
            }
            if (customer.getEmail().equals(email)) {
                throw new UserException(EMAIL_ALREADY_EXISTS);
            }
            if (customer.getPhoneNumber().equals(phoneNumber)) {
                throw new UserException(PHONE_NUMBER_ALREADY_EXISTS);
            }
        }

        Customer customer = signUpMapper.toCustomerEntity(req, bCryptPasswordEncoder);
        customerRepository.save(customer);

        return SignUp.Response.fromCustomerEntity(customer);
    }
}
