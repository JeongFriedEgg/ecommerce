package com.market.ecommerce.domain.payment.repository;

import com.market.ecommerce.domain.payment.entity.TossPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TossPaymentRepository extends JpaRepository<TossPayment, Long> {
}
