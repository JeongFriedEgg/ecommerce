package com.market.ecommerce.domain.user.entity.impl;

import com.market.ecommerce.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "seller")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Seller extends User {

    @Column(name = "account_number", length = 30)
    private String accountNumber;
}
