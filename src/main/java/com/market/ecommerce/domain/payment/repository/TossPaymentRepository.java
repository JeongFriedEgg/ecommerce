package com.market.ecommerce.domain.payment.repository;

import com.market.ecommerce.domain.payment.entity.TossPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TossPaymentRepository extends JpaRepository<TossPayment, Long> {
    Optional<TossPayment> findByPaymentKey(String paymentKey);
}
