package com.market.ecommerce.exception.domain.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrderErrorResponse {
    private int status;
    private String code;
    private String message;
    private List<String> details;
}
