package com.market.ecommerce.domain.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 사용자입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "로그인 인증에 실패하였습니다."),
    DUPLICATE_USER_FOUND(HttpStatus.CONFLICT,"중복된 계정이 존재합니다."),

    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT,"이미 사용중인 아이디입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT,"이미 사용중인 이메일입니다."),
    PHONE_NUMBER_ALREADY_EXISTS(HttpStatus.CONFLICT,"이미 사용중인 전화번호입니다.")
    ;

    private final HttpStatus status;
    private final String message;

    public int getStatusCode() {
        return this.status.value();
    }
}
