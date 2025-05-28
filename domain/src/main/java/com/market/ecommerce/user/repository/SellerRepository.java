package com.market.ecommerce.user.repository;

import com.market.ecommerce.user.entity.impl.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

    Optional<Seller> findByUsername(String username);

    Optional<Seller> findByEmailOrPhoneNumber(String email, String phoneNumber);
}
