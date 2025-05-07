package com.market.ecommerce.domain.user.service;

import com.market.ecommerce.domain.user.dto.CustomUserDetails;
import com.market.ecommerce.domain.user.entity.User;
import com.market.ecommerce.domain.user.exception.UserException;
import com.market.ecommerce.domain.user.repository.AdminRepository;
import com.market.ecommerce.domain.user.repository.CustomerRepository;
import com.market.ecommerce.domain.user.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.market.ecommerce.domain.user.exception.UserErrorCode.DUPLICATE_USER_FOUND;
import static com.market.ecommerce.domain.user.exception.UserErrorCode.USER_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUsername(username)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        return new CustomUserDetails(user);
    }

    private Optional<User> findUserByUsername(String username) {
        List<User> matchedUsers = new ArrayList<>();

        customerRepository.findByUsername(username).ifPresent(matchedUsers::add);
        sellerRepository.findByUsername(username).ifPresent(matchedUsers::add);
        adminRepository.findByUsername(username).ifPresent(matchedUsers::add);

        if (matchedUsers.size() > 1) {
            throw new UserException(DUPLICATE_USER_FOUND);
        }

        return matchedUsers.stream().findFirst();
    }
}
