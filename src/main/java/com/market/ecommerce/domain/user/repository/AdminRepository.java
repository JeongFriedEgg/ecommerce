package com.market.ecommerce.domain.user.repository;

import com.market.ecommerce.domain.user.entity.impl.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByUsername(String username);

    Optional<Admin> findByUsernameOrEmailOrPhoneNumber(String username, String email, String phoneNumber);
}
