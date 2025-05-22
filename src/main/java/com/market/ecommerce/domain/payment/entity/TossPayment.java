package com.market.ecommerce.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "toss_payment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value = {AuditingEntityListener.class})
public class TossPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String paymentKey; // 결제의 키값

    @Column(nullable = false)
    private String orderId; // 주문번호

    @Column(nullable = false)
    private String orderName;

    @Column(nullable = false)
    private Long totalAmount; // 총 결제 금액

    @Column(nullable = false)
    private String status; // 결제 처리 상태

    private String method; // 결제수단

    @Column(nullable = false)
    private String requestedAt; // 결제가 일어난 날짜와 시간 정보

    private String approvedAt; // 결제 승인이 일어난 날짜와 시간 정보
}
