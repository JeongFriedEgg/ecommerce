package com.market.ecommerce.exception.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MultiUserException extends RuntimeException {
    private final List<UserErrorCode> errorCodes;
}
