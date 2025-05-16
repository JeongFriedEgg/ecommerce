package com.market.ecommerce.domain.payment.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TestPaymentController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String WIDGET_SECRET_KEY = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
    private static final String API_SECRET_KEY = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
    private final Map<String, String> billingKeyMap = new HashMap<>();

    @RequestMapping(value = {"/confirm/widget", "/confirm/payment"})
    public ResponseEntity<JsonObject> confirmPayment(HttpServletRequest request, @RequestBody String jsonBody) throws Exception {
        String secretKey = request.getRequestURI().contains("/confirm/payment") ? API_SECRET_KEY : WIDGET_SECRET_KEY;
        JsonObject response = sendRequest(parseRequestData(jsonBody), secretKey, "https://api.tosspayments.com/v1/payments/confirm");
        int statusCode = response.has("error") ? 400 : 200;
        return ResponseEntity.status(statusCode).body(response);
    }

    @RequestMapping(value = "/confirm-billing")
    public ResponseEntity<JsonObject> confirmBilling(@RequestBody String jsonBody) throws Exception {
        JsonObject requestData = parseRequestData(jsonBody);
        String billingKey = billingKeyMap.get(requestData.get("customerKey").getAsString());
        JsonObject response = sendRequest(requestData, API_SECRET_KEY, "https://api.tosspayments.com/v1/billing/" + billingKey);
        return ResponseEntity.status(response.has("error") ? 400 : 200).body(response);
    }

    @RequestMapping(value = "/issue-billing-key")
    public ResponseEntity<JsonObject> issueBillingKey(@RequestBody String jsonBody) throws Exception {
        JsonObject requestData = parseRequestData(jsonBody);
        JsonObject response = sendRequest(requestData, API_SECRET_KEY, "https://api.tosspayments.com/v1/billing/authorizations/issue");

        if (!response.has("error")) {
            billingKeyMap.put(requestData.get("customerKey").getAsString(), response.get("billingKey").getAsString());
        }

        return ResponseEntity.status(response.has("error") ? 400 : 200).body(response);
    }

    @RequestMapping(value = "/callback-auth", method = RequestMethod.GET)
    public ResponseEntity<JsonObject> callbackAuth(@RequestParam String customerKey, @RequestParam String code) throws Exception {
        JsonObject requestData = new JsonObject();
        requestData.addProperty("grantType", "AuthorizationCode");
        requestData.addProperty("customerKey", customerKey);
        requestData.addProperty("code", code);

        String url = "https://api.tosspayments.com/v1/brandpay/authorizations/access-token";
        JsonObject response = sendRequest(requestData, API_SECRET_KEY, url);

        logger.info("Response Data: {}", response);

        return ResponseEntity.status(response.has("error") ? 400 : 200).body(response);
    }

    @RequestMapping(value = "/confirm/brandpay", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<JsonObject> confirmBrandpay(@RequestBody String jsonBody) throws Exception {
        JsonObject requestData = parseRequestData(jsonBody);
        String url = "https://api.tosspayments.com/v1/brandpay/payments/confirm";
        JsonObject response = sendRequest(requestData, API_SECRET_KEY, url);
        return ResponseEntity.status(response.has("error") ? 400 : 200).body(response);
    }

    private JsonObject parseRequestData(String jsonBody) {
        try {
            return JsonParser.parseString(jsonBody).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            logger.error("JSON Parsing Error", e);
            return new JsonObject();
        }
    }

    private JsonObject sendRequest(JsonObject requestData, String secretKey, String urlString) throws IOException {
        HttpURLConnection connection = createConnection(secretKey, urlString);
        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestData.toString().getBytes(StandardCharsets.UTF_8));
        }

        try (InputStream responseStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream();
             Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (Exception e) {
            logger.error("Error reading response", e);
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("error", "Error reading response");
            return errorResponse;
        }
    }

    private HttpURLConnection createConnection(String secretKey, String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8)));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        return connection;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "/widget/checkout";
    }

    @RequestMapping(value = "/fail", method = RequestMethod.GET)
    public String failPayment(HttpServletRequest request, Model model) {
        model.addAttribute("code", request.getParameter("code"));
        model.addAttribute("message", request.getParameter("message"));
        return "/fail";
    }
}