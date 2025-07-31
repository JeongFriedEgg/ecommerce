package com.market.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CartApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartApiApplication.class, args);
    }
}
