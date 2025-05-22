package com.market.ecommerce.domain.payment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TossPaymentErrorCode {

    TOSS_PAYMENT_AUTHORIZATION_INFO_MISMATCH(HttpStatus.BAD_REQUEST,"결제 정보가 일치하지 않습니다."),
    NOT_FOUND_PAYMENT(HttpStatus.NOT_FOUND, "존재하지 않는 결제입니다."),

    TOSS_API_RESPONSE_PARSE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"토스페이먼츠 에러 응답 파싱 실패"),
    TOSS_API_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"토스페이먼츠 내부 시스템 오류"),
    ;
    private final HttpStatus status;
    private final String message;

    public int getStatusCode() {
        return this.status.value();
    }
}
