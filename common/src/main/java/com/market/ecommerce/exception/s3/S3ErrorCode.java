package com.market.ecommerce.exception.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum S3ErrorCode {

    FILE_S3_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3 처리 중 오류가 발생했습니다.")
    ;
    private final HttpStatus status;
    private final String message;

    public int getStatusCode() {
        return this.status.value();
    }
}
