package com.market.ecommerce.domain.user.security;

import com.market.ecommerce.domain.user.dto.CustomUserDetails;
import com.market.ecommerce.domain.user.entity.User;
import com.market.ecommerce.domain.user.entity.impl.Admin;
import com.market.ecommerce.domain.user.entity.impl.Customer;
import com.market.ecommerce.domain.user.entity.impl.Seller;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];
        if (jwtProvider.isExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtProvider.getUsername(token);
        String role = jwtProvider.getRole(token);

        User user = null;
        if (role.equals("ROLE_SELLER")) {
            user = Seller.builder().username(username).build();
        } else if (role.equals("ROLE_ADMIN")) {
            user = Admin.builder().username(username).build();
        } else if (role.equals("ROLE_CUSTOMER")){
            user = Customer.builder().username(username).build();
        }
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails,
                null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
