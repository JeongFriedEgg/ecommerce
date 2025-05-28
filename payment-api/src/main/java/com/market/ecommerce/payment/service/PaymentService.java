package com.market.ecommerce.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.ecommerce.exception.domain.payment.*;
import com.market.ecommerce.payment.dto.PaymentConfirm;
import com.market.ecommerce.payment.entity.TossPayment;
import com.market.ecommerce.payment.mapper.TossPaymentMapper;
import com.market.ecommerce.payment.repository.TossPaymentRepository;
import com.market.ecommerce.payment.service.request.TossPaymentCancelRequest;
import com.market.ecommerce.payment.service.response.TossPaymentResponse;
import com.market.ecommerce.payment.service.request.TossPaymentConfirmRequest;
import com.market.ecommerce.payment.service.response.TossErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentService {
    @Value("${toss.secret-key}")
    private String secretKey;
    private static final String TOSS_CLIENT_URI = "https://api.tosspayments.com/v1/payments";

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final TossPaymentRepository tossPaymentRepository;
    private final TossPaymentMapper tossPaymentMapper;


    public void getPayment(PaymentConfirm.Request req) {
        String paymentKey = req.getPaymentKey();
        String orderId = req.getOrderId();
        String amount = req.getAmount();
        TossPaymentResponse res = restClient.get()
                .uri(TOSS_CLIENT_URI + "/{paymentKey}", paymentKey)
                .header(HttpHeaders.AUTHORIZATION, createAuthorizationHeader())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    TossErrorResponse errorResponse = parseClientErrorResponse(response);
                    TossGetPaymentErrorCode code = mapTossGetPaymentErrorCodeToEnum(response.getStatusCode(), errorResponse.getError().getCode());
                    throw new TossGetPaymentException(code);
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    TossErrorResponse errorResponse = parseClientErrorResponse(response);
                    TossGetPaymentErrorCode code = mapTossGetPaymentErrorCodeToEnum(response.getStatusCode(), errorResponse.getError().getCode());
                    throw new TossGetPaymentException(code);
                })
                .body(TossPaymentResponse.class);

        boolean isMismatched = !Objects.equals(paymentKey, res.getPaymentKey())
                || !Objects.equals(orderId, res.getOrderId())
                || !Objects.equals(amount, String.valueOf(res.getTotalAmount()));

        if (isMismatched) {
            throw new TossPaymentException(TossPaymentErrorCode.TOSS_PAYMENT_AUTHORIZATION_INFO_MISMATCH);
        }
    }

    @Transactional
    public TossPaymentResponse confirmPayment(String paymentKey, String orderId, Long amount) {
        TossPaymentConfirmRequest confirmRequest = new TossPaymentConfirmRequest(paymentKey, orderId, amount);

        TossPaymentResponse res =  restClient.post()
                .uri(TOSS_CLIENT_URI+"/confirm")
                .header(HttpHeaders.AUTHORIZATION, createAuthorizationHeader())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .body(confirmRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    TossErrorResponse errorResponse = parseClientErrorResponse(response);
                    TossConfirmPaymentErrorCode code = mapTossConfirmPaymentErrorCodeToEnum(response.getStatusCode(),errorResponse.getError().getCode());
                    throw new TossConfirmPaymentException(code);
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    TossErrorResponse errorResponse = parseClientErrorResponse(response);
                    TossConfirmPaymentErrorCode code = mapTossConfirmPaymentErrorCodeToEnum(response.getStatusCode(),errorResponse.getError().getCode());
                    throw new TossConfirmPaymentException(code);
                })
                .body(TossPaymentResponse.class);

        TossPayment tossPayment = tossPaymentMapper.toEntity(res);
        tossPaymentRepository.save(tossPayment);

        return res;
    }

    @Transactional
    public TossPaymentResponse cancelPayment(String paymentKey, String cancelReason) {
        TossPaymentCancelRequest cancelRequest = new TossPaymentCancelRequest(cancelReason);

        TossPaymentResponse res = restClient.post()
                .uri(TOSS_CLIENT_URI + "/{paymentKey}",paymentKey)
                .header(HttpHeaders.AUTHORIZATION, createAuthorizationHeader())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .body(cancelRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,(request, response) -> {
                    TossErrorResponse errorResponse = parseClientErrorResponse(response);
                    TossCancelPaymentErrorCode code = mapTossCancelPaymentErrorCodeToEnum(response.getStatusCode(),errorResponse.getError().getCode());
                    throw new TossCancelPaymentException(code);
                })
                .onStatus(HttpStatusCode::is5xxServerError,(request, response) -> {
                    TossErrorResponse errorResponse = parseClientErrorResponse(response);
                    TossCancelPaymentErrorCode code = mapTossCancelPaymentErrorCodeToEnum(response.getStatusCode(),errorResponse.getError().getCode());
                    throw new TossCancelPaymentException(code);
                })
                .body(TossPaymentResponse.class);

        TossPayment tossPayment = tossPaymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new TossPaymentException(TossPaymentErrorCode.NOT_FOUND_PAYMENT));

        tossPayment.setStatus(res.getStatus());
        if (res.getCancels() != null && !res.getCancels().isEmpty()) {
            TossPaymentResponse.Cancels firstCancel = res.getCancels().get(0);
            tossPayment.setCancelAt(firstCancel.getCanceledAt());
            tossPayment.setCancelReason(firstCancel.getCancelReason());
        }
        tossPaymentRepository.save(tossPayment);

        return res;
    }

    private String createAuthorizationHeader() {
        String encoded = Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        return "Basic " + encoded;
    }

    private TossErrorResponse parseClientErrorResponse(ClientHttpResponse response) {
        try (InputStream errorStream = response.getBody()) {
            return objectMapper.readValue(errorStream, TossErrorResponse.class);
        } catch (IOException e) {
            throw new TossPaymentException(TossPaymentErrorCode.TOSS_API_RESPONSE_PARSE_ERROR);
        }
    }

    private TossGetPaymentErrorCode mapTossGetPaymentErrorCodeToEnum(HttpStatusCode status, String tossCode) {
        return switch (status.value()) {
            case 400 -> switch (tossCode) {
                case "NOT_SUPPORTED_MONTHLY_INSTALLMENT_PLAN_BELOW_AMOUNT"
                        -> TossGetPaymentErrorCode.NOT_SUPPORTED_MONTHLY_INSTALLMENT_PLAN_BELOW_AMOUNT;
                default -> TossGetPaymentErrorCode.NOT_FOUND;
            };
            case 401 -> TossGetPaymentErrorCode.UNAUTHORIZED_KEY;
            case 403 -> switch (tossCode) {
                case "FORBIDDEN_CONSECUTIVE_REQUEST" -> TossGetPaymentErrorCode.FORBIDDEN_CONSECUTIVE_REQUEST;
                case "INCORRECT_BASIC_AUTH_FORMAT" -> TossGetPaymentErrorCode.INCORRECT_BASIC_AUTH_FORMAT;
                default -> TossGetPaymentErrorCode.NOT_FOUND;
            };
            case 404 -> switch (tossCode) {
                case "NOT_FOUND_PAYMENT" -> TossGetPaymentErrorCode.NOT_FOUND_PAYMENT;
                default -> TossGetPaymentErrorCode.NOT_FOUND;
            };
            default -> TossGetPaymentErrorCode.FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING;
        };
    }


    private TossConfirmPaymentErrorCode mapTossConfirmPaymentErrorCodeToEnum(HttpStatusCode status, String tossCode) {
        return switch (status.value()) {
            case 400 -> switch (tossCode) {
                case "ALREADY_PROCESSED_PAYMENT" -> TossConfirmPaymentErrorCode.ALREADY_PROCESSED_PAYMENT;
                case "PROVIDER_ERROR" -> TossConfirmPaymentErrorCode.PROVIDER_ERROR;
                case "EXCEED_MAX_CARD_INSTALLMENT_PLAN" -> TossConfirmPaymentErrorCode.EXCEED_MAX_CARD_INSTALLMENT_PLAN;
                case "INVALID_REQUEST" -> TossConfirmPaymentErrorCode.INVALID_REQUEST;
                case "NOT_ALLOWED_POINT_USE" -> TossConfirmPaymentErrorCode.NOT_ALLOWED_POINT_USE;
                case "INVALID_API_KEY" -> TossConfirmPaymentErrorCode.INVALID_API_KEY;
                case "INVALID_REJECT_CARD" -> TossConfirmPaymentErrorCode.INVALID_REJECT_CARD;
                case "BELOW_MINIMUM_AMOUNT" -> TossConfirmPaymentErrorCode.BELOW_MINIMUM_AMOUNT;
                case "INVALID_CARD_EXPIRATION" -> TossConfirmPaymentErrorCode.INVALID_CARD_EXPIRATION;
                case "INVALID_STOPPED_CARD" -> TossConfirmPaymentErrorCode.INVALID_STOPPED_CARD;
                case "EXCEED_MAX_DAILY_PAYMENT_COUNT" -> TossConfirmPaymentErrorCode.EXCEED_MAX_DAILY_PAYMENT_COUNT;
                case "NOT_SUPPORTED_INSTALLMENT_PLAN_CARD_OR_MERCHANT" -> TossConfirmPaymentErrorCode.NOT_SUPPORTED_INSTALLMENT_PLAN_CARD_OR_MERCHANT;
                case "INVALID_CARD_INSTALLMENT_PLAN" -> TossConfirmPaymentErrorCode.INVALID_CARD_INSTALLMENT_PLAN;
                case "NOT_SUPPORTED_MONTHLY_INSTALLMENT_PLAN" -> TossConfirmPaymentErrorCode.NOT_SUPPORTED_MONTHLY_INSTALLMENT_PLAN;
                case "EXCEED_MAX_PAYMENT_AMOUNT" -> TossConfirmPaymentErrorCode.EXCEED_MAX_PAYMENT_AMOUNT;
                case "NOT_FOUND_TERMINAL_ID" -> TossConfirmPaymentErrorCode.NOT_FOUND_TERMINAL_ID;
                case "INVALID_AUTHORIZE_AUTH" -> TossConfirmPaymentErrorCode.INVALID_AUTHORIZE_AUTH;
                case "INVALID_CARD_LOST_OR_STOLEN" -> TossConfirmPaymentErrorCode.INVALID_CARD_LOST_OR_STOLEN;
                case "RESTRICTED_TRANSFER_ACCOUNT" -> TossConfirmPaymentErrorCode.RESTRICTED_TRANSFER_ACCOUNT;
                case "INVALID_CARD_NUMBER" -> TossConfirmPaymentErrorCode.INVALID_CARD_NUMBER;
                case "INVALID_UNREGISTERED_SUBMALL" -> TossConfirmPaymentErrorCode.INVALID_UNREGISTERED_SUBMALL;
                case "NOT_REGISTERED_BUSINESS" -> TossConfirmPaymentErrorCode.NOT_REGISTERED_BUSINESS;
                case "EXCEED_MAX_ONE_DAY_WITHDRAW_AMOUNT" -> TossConfirmPaymentErrorCode.EXCEED_MAX_ONE_DAY_WITHDRAW_AMOUNT;
                case "EXCEED_MAX_ONE_TIME_WITHDRAW_AMOUNT" -> TossConfirmPaymentErrorCode.EXCEED_MAX_ONE_TIME_WITHDRAW_AMOUNT;
                case "CARD_PROCESSING_ERROR" -> TossConfirmPaymentErrorCode.CARD_PROCESSING_ERROR;
                case "EXCEED_MAX_AMOUNT" -> TossConfirmPaymentErrorCode.EXCEED_MAX_AMOUNT;
                case "INVALID_ACCOUNT_INFO_RE_REGISTER" -> TossConfirmPaymentErrorCode.INVALID_ACCOUNT_INFO_RE_REGISTER;
                case "NOT_AVAILABLE_PAYMENT" -> TossConfirmPaymentErrorCode.NOT_AVAILABLE_PAYMENT;
                case "UNAPPROVED_ORDER_ID" -> TossConfirmPaymentErrorCode.UNAPPROVED_ORDER_ID;
                case "EXCEED_MAX_MONTHLY_PAYMENT_AMOUNT" -> TossConfirmPaymentErrorCode.EXCEED_MAX_MONTHLY_PAYMENT_AMOUNT;
                default -> TossConfirmPaymentErrorCode.UNKNOWN_PAYMENT_ERROR;
            };
            case 401 -> switch (tossCode) {
                case "UNAUTHORIZED_KEY" -> TossConfirmPaymentErrorCode.UNAUTHORIZED_KEY;
                default -> TossConfirmPaymentErrorCode.UNKNOWN_PAYMENT_ERROR;
            };
            case 403 -> switch (tossCode) {
                case "REJECT_ACCOUNT_PAYMENT" -> TossConfirmPaymentErrorCode.REJECT_ACCOUNT_PAYMENT;
                case "REJECT_CARD_PAYMENT" -> TossConfirmPaymentErrorCode.REJECT_CARD_PAYMENT;
                case "REJECT_CARD_COMPANY" -> TossConfirmPaymentErrorCode.REJECT_CARD_COMPANY;
                case "FORBIDDEN_REQUEST" -> TossConfirmPaymentErrorCode.FORBIDDEN_REQUEST;
                case "REJECT_TOSSPAY_INVALID_ACCOUNT" -> TossConfirmPaymentErrorCode.REJECT_TOSSPAY_INVALID_ACCOUNT;
                case "EXCEED_MAX_AUTH_COUNT" -> TossConfirmPaymentErrorCode.EXCEED_MAX_AUTH_COUNT;
                case "EXCEED_MAX_ONE_DAY_AMOUNT" -> TossConfirmPaymentErrorCode.EXCEED_MAX_ONE_DAY_AMOUNT;
                case "NOT_AVAILABLE_BANK" -> TossConfirmPaymentErrorCode.NOT_AVAILABLE_BANK;
                case "INVALID_PASSWORD" -> TossConfirmPaymentErrorCode.INVALID_PASSWORD;
                case "INCORRECT_BASIC_AUTH_FORMAT" -> TossConfirmPaymentErrorCode.INCORRECT_BASIC_AUTH_FORMAT;
                case "FDS_ERROR" -> TossConfirmPaymentErrorCode.FDS_ERROR;
                default -> TossConfirmPaymentErrorCode.UNKNOWN_PAYMENT_ERROR;
            };
            case 404 -> switch (tossCode) {
                case "NOT_FOUND_PAYMENT" -> TossConfirmPaymentErrorCode.NOT_FOUND_PAYMENT;
                case "NOT_FOUND_PAYMENT_SESSION" -> TossConfirmPaymentErrorCode.NOT_FOUND_PAYMENT_SESSION;
                default -> TossConfirmPaymentErrorCode.UNKNOWN_PAYMENT_ERROR;
            };
            case 500 -> switch (tossCode) {
                case "FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING" -> TossConfirmPaymentErrorCode.FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING;
                case "FAILED_INTERNAL_SYSTEM_PROCESSING" -> TossConfirmPaymentErrorCode.FAILED_INTERNAL_SYSTEM_PROCESSING;
                default -> TossConfirmPaymentErrorCode.UNKNOWN_PAYMENT_ERROR;
            };
            default -> TossConfirmPaymentErrorCode.UNKNOWN_PAYMENT_ERROR;
        };
    }

    private TossCancelPaymentErrorCode mapTossCancelPaymentErrorCodeToEnum(HttpStatusCode status, String tossCode) {
        return switch (status.value()) {
            case 400 -> switch (tossCode) {
                case "ALREADY_CANCELED_PAYMENT" -> TossCancelPaymentErrorCode.ALREADY_CANCELED_PAYMENT;
                case "INVALID_REFUND_ACCOUNT_INFO" -> TossCancelPaymentErrorCode.INVALID_REFUND_ACCOUNT_INFO;
                case "EXCEED_CANCEL_AMOUNT_DISCOUNT_AMOUNT" -> TossCancelPaymentErrorCode.EXCEED_CANCEL_AMOUNT_DISCOUNT_AMOUNT;
                case "INVALID_REQUEST" -> TossCancelPaymentErrorCode.INVALID_REQUEST;
                case "INVALID_REFUND_ACCOUNT_NUMBER" -> TossCancelPaymentErrorCode.INVALID_REFUND_ACCOUNT_NUMBER;
                case "INVALID_BANK" -> TossCancelPaymentErrorCode.INVALID_BANK;
                case "NOT_MATCHES_REFUNDABLE_AMOUNT" -> TossCancelPaymentErrorCode.NOT_MATCHES_REFUNDABLE_AMOUNT;
                case "PROVIDER_ERROR" -> TossCancelPaymentErrorCode.PROVIDER_ERROR;
                case "REFUND_REJECTED" -> TossCancelPaymentErrorCode.REFUND_REJECTED;
                case "ALREADY_REFUND_PAYMENT" -> TossCancelPaymentErrorCode.ALREADY_REFUND_PAYMENT;
                case "FORBIDDEN_BANK_REFUND_REQUEST" -> TossCancelPaymentErrorCode.FORBIDDEN_BANK_REFUND_REQUEST;
                default -> TossCancelPaymentErrorCode.COMMON_ERROR;
            };
            case 401 -> switch (tossCode) {
                case "UNAUTHORIZED_KEY" -> TossCancelPaymentErrorCode.UNAUTHORIZED_KEY;
                default -> TossCancelPaymentErrorCode.COMMON_ERROR;
            };
            case 403 -> switch (tossCode) {
                case "NOT_CANCELABLE_AMOUNT" -> TossCancelPaymentErrorCode.NOT_CANCELABLE_AMOUNT;
                case "FORBIDDEN_CONSECUTIVE_REQUEST" -> TossCancelPaymentErrorCode.FORBIDDEN_CONSECUTIVE_REQUEST;
                case "FORBIDDEN_REQUEST" -> TossCancelPaymentErrorCode.FORBIDDEN_REQUEST;
                case "NOT_CANCELABLE_PAYMENT" -> TossCancelPaymentErrorCode.NOT_CANCELABLE_PAYMENT;
                case "EXCEED_MAX_REFUND_DUE" -> TossCancelPaymentErrorCode.EXCEED_MAX_REFUND_DUE;
                case "NOT_ALLOWED_PARTIAL_REFUND_WAITING_DEPOSIT" -> TossCancelPaymentErrorCode.NOT_ALLOWED_PARTIAL_REFUND_WAITING_DEPOSIT;
                case "NOT_ALLOWED_PARTIAL_REFUND" -> TossCancelPaymentErrorCode.NOT_ALLOWED_PARTIAL_REFUND;
                case "NOT_AVAILABLE_BANK" -> TossCancelPaymentErrorCode.NOT_AVAILABLE_BANK;
                case "INCORRECT_BASIC_AUTH_FORMAT" -> TossCancelPaymentErrorCode.INCORRECT_BASIC_AUTH_FORMAT;
                case "NOT_CANCELABLE_PAYMENT_FOR_DORMANT_USER" -> TossCancelPaymentErrorCode.NOT_CANCELABLE_PAYMENT_FOR_DORMANT_USER;
                default -> TossCancelPaymentErrorCode.COMMON_ERROR;
            };
            case 404 -> switch (tossCode) {
                case "NOT_FOUND_PAYMENT" -> TossCancelPaymentErrorCode.NOT_FOUND_PAYMENT;
                default -> TossCancelPaymentErrorCode.COMMON_ERROR;
            };
            case 500 -> switch (tossCode) {
                case "FAILED_INTERNAL_SYSTEM_PROCESSING" -> TossCancelPaymentErrorCode.FAILED_INTERNAL_SYSTEM_PROCESSING;
                case "FAILED_REFUND_PROCESS" -> TossCancelPaymentErrorCode.FAILED_REFUND_PROCESS;
                case "FAILED_METHOD_HANDLING_CANCEL" -> TossCancelPaymentErrorCode.FAILED_METHOD_HANDLING_CANCEL;
                case "FAILED_PARTIAL_REFUND" -> TossCancelPaymentErrorCode.FAILED_PARTIAL_REFUND;
                case "FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING" -> TossCancelPaymentErrorCode.FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING;
                case "FAILED_CLOSED_ACCOUNT_REFUND" -> TossCancelPaymentErrorCode.FAILED_CLOSED_ACCOUNT_REFUND;
                default -> TossCancelPaymentErrorCode.COMMON_ERROR;
            };
            default -> TossCancelPaymentErrorCode.COMMON_ERROR;
        };
    }
}
