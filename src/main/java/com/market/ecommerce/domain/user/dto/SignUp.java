package com.market.ecommerce.domain.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.market.ecommerce.domain.user.entity.impl.Admin;
import com.market.ecommerce.domain.user.entity.impl.Customer;
import com.market.ecommerce.domain.user.entity.impl.Seller;
import lombok.Builder;
import lombok.Getter;

public class SignUp {

    @Getter
    public static class Request {
        private String id;
        private String password;
        private String email;
        private String phoneNumber;
    }

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CustomerRequest extends Request {
        private String address;
    }

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SellerRequest extends Request {
        private String accountNumber;
    }

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class AdminRequest extends Request {

    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Response {
        private String id;
        private String createdDate;

        public static Response fromCustomerEntity(Customer customer) {
            return Response.builder()
                    .id(customer.getUsername())
                    .createdDate(customer.getCreatedAt().toString())
                    .build();
        }

        public static Response fromSellerEntity(Seller seller) {
            return Response.builder()
                    .id(seller.getUsername())
                    .createdDate(seller.getCreatedAt().toString())
                    .build();
        }

        public static Response fromAdminEntity(Admin admin) {
            return Response.builder()
                    .id(admin.getUsername())
                    .createdDate(admin.getCreatedAt().toString())
                    .build();
        }
    }
}
