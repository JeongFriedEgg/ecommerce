package com.market.ecommerce.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    public RestClient restClient() {
        return RestClient.create();
    }
}
