package com.market.ecommerce.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NULL_POINTER(HttpStatus.BAD_REQUEST, "널 포인터 예외가 발생했습니다."),
    NUMBER_FORMAT(HttpStatus.BAD_REQUEST, "숫자 포맷 오류가 발생했습니다."),
    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "잘못된 인자가 전달되었습니다."),
    ILLEGAL_STATE(HttpStatus.BAD_REQUEST, "잘못된 객체 상태입니다."),
    NO_SUCH_METHOD(HttpStatus.BAD_REQUEST, "존재하지 않는 메소드 호출이 시도되었습니다."),
    CLASS_CAST(HttpStatus.BAD_REQUEST, "잘못된 형변환이 시도되었습니다."),
    PARSE_EXCEPTION(HttpStatus.BAD_REQUEST, "파싱 오류가 발생했습니다."),
    INVOCATION_TARGET(HttpStatus.BAD_REQUEST, "메소드 호출 오류가 발생했습니다."),

    RUNTIME_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "런타임 예외가 발생했습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버 오류가 발생했습니다.")
    ;

    private final HttpStatus status;
    private final String message;

    public int getStatusCode() {
        return this.status.value();
    }
}
