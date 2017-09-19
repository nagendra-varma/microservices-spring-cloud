package com.techo.productcompose;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableFeignClients
public class ProductComposeApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ProductComposeApplication.class)
                .web(true)
                .build()
                .run(args);
    }
}
