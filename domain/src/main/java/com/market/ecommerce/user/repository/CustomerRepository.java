package com.market.ecommerce.user.repository;

import com.market.ecommerce.user.entity.impl.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByUsername(String username);

    Optional<Customer> findByEmailOrPhoneNumber(String email, String phoneNumber);
}
