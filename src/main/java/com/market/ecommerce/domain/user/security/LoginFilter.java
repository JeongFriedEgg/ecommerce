package com.market.ecommerce.domain.user.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.ecommerce.domain.user.dto.CustomUserDetails;
import com.market.ecommerce.domain.user.dto.LogIn;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;


@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {

        LogIn.Request loginReq;

        try {
            loginReq = objectMapper.readValue(request.getInputStream(), LogIn.Request.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword(), null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain, Authentication authentication) throws IOException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();
        log.info("role : {}",role);

        String token = jwtProvider.createJwtToken(
                userDetails.getUsername(),
                role,
                60 * 50 * 10L);

        LogIn.Response resDto = LogIn.Response.builder()
                .message("로그인 성공")
                .build();
        response.addHeader("Authorization","Bearer " + token);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String responseBody = objectMapper.writeValueAsString(resDto);
        response.getWriter().write(responseBody);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException failed) throws IOException {
        LogIn.Response resDto = LogIn.Response.builder()
                .message("로그인 실패: " + failed.getMessage())
                .build();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String responseBody = objectMapper.writeValueAsString(resDto);
        response.getWriter().write(responseBody);
    }
}
