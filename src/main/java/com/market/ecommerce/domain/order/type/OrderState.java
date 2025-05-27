package com.market.ecommerce.domain.order.type;

public enum OrderState {
    CREATED,            // 주문 생성됨
    PAYMENT_PENDING,    // 결제 대기중
    PAID,               // 결제 완료
    PREPARING,          // 상품 준비 중
    SHIPPED,            // 배송 시작됨
    DELIVERED,          // 배송 완료
    COMPLETED,          // 구매 확정
    CANCELED,           // 주문 취소
    REFUND_REQUESTED,   // 반품 요청
    REFUNDED            // 환불 완료
}
