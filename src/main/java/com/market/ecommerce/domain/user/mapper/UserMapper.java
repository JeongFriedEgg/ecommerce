package com.market.ecommerce.domain.user.mapper;

import com.market.ecommerce.domain.user.dto.SignUp;
import com.market.ecommerce.domain.user.entity.User;
import com.market.ecommerce.domain.user.type.UserType;

import java.time.LocalDateTime;

public class UserMapper {
    public static User toEntity(SignUp.Request dto) {
        return User.builder()
                .userId(dto.getUserId())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .role(dto.getRole() != null ? UserType.valueOf(dto.getRole()) : UserType.CUSTOMER)
                .address(dto.getAddress())
                .createdAt(LocalDateTime.now().withNano(0))
                .updatedAt(LocalDateTime.now().withNano(0))
                .build();
    }
}
