package com.market.ecommerce.common.exception.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RedisErrorCode {

    JSON_SERIALIZE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"JSON 직렬화에 실패했습니다."),
    JSON_DESERIALIZE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"JSON 역직렬화에 실패했습니다.")
    ;

    private final HttpStatus status;
    private final String message;

    public int getStatusCode() {
        return this.status.value();
    }
}
