package com.market.ecommerce.domain.payment.service.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TossPaymentResponse {
    private String mId;
    private String lastTransactionKey;
    private String paymentKey;
    private String orderId;
    private String orderName;
    private Long taxExemptionAmount;
    private String status; // 결제 처리 상태
    private String requestedAt;
    private String approvedAt;
    private Boolean useEscrow;
    private Boolean cultureExpense;
    private Card card; // 카드 정보

    private List<Cancels> cancels;

    private String type; // NORMAL, BRANDPAY 등
    private EasyPay easyPay; // 간편결제 정보
    private String country;
    private Failure failure; // 실패 정보
    private Boolean isPartialCancelable;
    private Receipt receipt;
    private Checkout checkout;
    private String currency;
    private Long totalAmount;
    private Long balanceAmount;
    private Long suppliedAmount;
    private Long vat;
    private Long taxFreeAmount;
    private String method; // 결제 수단 (카드, 가상계좌 등)
    private String version;

    // 중첩 객체 (Card, EasyPay, Failure, Receipt, Checkout) 정의
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Card {
        private String issuerCode;
        private String acquirerCode;
        private String number;
        private Integer installmentPlanMonths;
        private Boolean isInterestFree;
        private String interestPayer;
        private String approveNo;
        private Boolean useCardPoint;
        private String cardType;
        private String ownerType;
        private String acquireStatus;
        private Long amount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Cancels {
        private String transactionKey;
        private String cancelReason;
        private Long taxExemptionAmount;
        private String canceledAt;
        private Long transferDiscountAmount;
        private Long easyPayDiscountAmount;
        private String receiptKey;
        private Long cancelAmount;
        private Long taxFreeAmount;
        private Long refundableAmount;
        private String cancelStatus;
        private String cancelRequestId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EasyPay {
        private String provider;
        private Long amount;
        private Long discountAmount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Failure {
        private String code;
        private String message;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Receipt {
        private String url;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Checkout {
        private String url;
    }
}
