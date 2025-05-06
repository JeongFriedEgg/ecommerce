package com.market.ecommerce.domain.user.service;

import com.market.ecommerce.domain.user.dto.CustomUserDetails;
import com.market.ecommerce.domain.user.entity.impl.Customer;
import com.market.ecommerce.domain.user.exception.UserException;
import com.market.ecommerce.domain.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import static com.market.ecommerce.domain.user.exception.UserErrorCode.USER_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        return new CustomUserDetails(customer);
    }
}
