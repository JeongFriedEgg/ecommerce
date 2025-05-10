package com.market.ecommerce.common.exception.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FileErrorCode {

    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"파일 업로드에 실패했습니다."),
    FILE_URL_CONVERSION_FAILED(HttpStatus.BAD_REQUEST,"이미지 URL을 변환하는 데 실패했습니다.")
    ;

    private final HttpStatus status;
    private final String message;

    public int getStatusCode() {
        return this.status.value();
    }
}
