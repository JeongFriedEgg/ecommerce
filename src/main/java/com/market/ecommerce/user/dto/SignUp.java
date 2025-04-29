package com.market.ecommerce.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.market.ecommerce.user.domain.User;
import lombok.*;

public class SignUp {

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SignUpRequest {
        private String userId;
        private String password;
        private String email;
        private String phoneNumber;
        private String role;
        private String address;
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SignUpResponse {
        private String userId;
        private String createdDate;

        public static SignUpResponse from(User user) {
            return SignUpResponse.builder()
                    .userId(user.getUserId())
                    .createdDate(user.getCreatedAt().toString())
                    .build();
        }
    }
}
