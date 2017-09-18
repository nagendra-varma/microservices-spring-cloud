package com.techo.servicedisc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ProductServiceDiscoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceDiscoveryApplication.class, args);
    }
}
