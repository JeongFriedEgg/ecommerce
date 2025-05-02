package com.market.ecommerce.domain.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.market.ecommerce.domain.user.entity.User;
import lombok.*;

public class SignUp {

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Request {
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
    public static class Response {
        private String userId;
        private String createdDate;

        public static Response fromEntity(User user) {
            return Response.builder()
                    .userId(user.getUserId())
                    .createdDate(user.getCreatedAt().toString())
                    .build();
        }
    }
}
