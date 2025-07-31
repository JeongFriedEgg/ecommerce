package com.market.ecommerce.user.entity.impl;

import com.market.ecommerce.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "admin")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Admin extends User {

}
