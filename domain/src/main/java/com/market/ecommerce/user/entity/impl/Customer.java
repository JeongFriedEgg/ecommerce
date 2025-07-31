package com.market.ecommerce.user.entity.impl;

import com.market.ecommerce.order.entity.Order;
import com.market.ecommerce.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Customer extends User {

    @Column(columnDefinition = "TEXT")
    private String address;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
}