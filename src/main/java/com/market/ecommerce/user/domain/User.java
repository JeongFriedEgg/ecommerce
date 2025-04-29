package com.market.ecommerce.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.market.ecommerce.user.dto.SignUp;
import com.market.ecommerce.user.type.UserType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "User")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value = {AuditingEntityListener.class})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String userId;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserType role = UserType.CUSTOMER;

    @Column(columnDefinition = "TEXT")
    private String address;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public static User from(SignUp.SignUpRequest req) {
        return User.builder()
                .userId(req.getUserId())
                .password(req.getPassword())
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .role(req.getRole() != null ? UserType.valueOf(req.getRole()) : UserType.CUSTOMER)
                .address(req.getAddress())
                .createdAt(LocalDateTime.now().withNano(0))
                .updatedAt(LocalDateTime.now().withNano(0))
                .build();
    }
}
