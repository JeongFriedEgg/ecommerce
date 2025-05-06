package com.market.ecommerce.domain.user.entity.impl;

import com.market.ecommerce.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Customer extends User {

    @Column(columnDefinition = "TEXT")
    private String address;
}