package com.market.ecommerce.user.service;

import com.market.ecommerce.user.entity.impl.Customer;
import com.market.ecommerce.user.repository.CustomerRepository;
import com.market.ecommerce.user.dto.SignUp;
import com.market.ecommerce.exception.domain.user.UserException;
import com.market.ecommerce.user.mapper.SignUpMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final SignUpMapper signUpMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void checkUsernameDuplication(String username) {
        Optional<Customer> existing = customerRepository.findByUsername(username);
        if (existing.isPresent()) {
            throw new UserException(USERNAME_ALREADY_EXISTS);
        }
    }

    public void checkEmailOrPhoneDuplication(String email, String phoneNumber) {
        Optional<Customer> existing = customerRepository.findByEmailOrPhoneNumber(email, phoneNumber);
        if (existing.isPresent()) {
            Customer customer = existing.get();
            if (customer.getEmail().equals(email)) {
                throw new UserException(EMAIL_ALREADY_EXISTS);
            }
            if (customer.getPhoneNumber().equals(phoneNumber)) {
                throw new UserException(PHONE_NUMBER_ALREADY_EXISTS);
            }
        }
    }

    public SignUp.Response register(SignUp.CustomerRequest req) {
        Customer customer = signUpMapper.toCustomerEntity(req, bCryptPasswordEncoder);
        customerRepository.save(customer);

        return SignUp.Response.fromCustomerEntity(customer);
    }

    public void findCustomerByUsername(String customerId) {
        customerRepository.findByUsername(customerId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    public Customer getCustomerByUsername(String customerId) {
        return customerRepository.findByUsername(customerId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }
}
