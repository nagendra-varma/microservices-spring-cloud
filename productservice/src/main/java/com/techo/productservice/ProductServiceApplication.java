package com.techo.productservice;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ProductServiceApplication {

    public static void main(String[] args) {
       new SpringApplicationBuilder(ProductServiceApplication.class)
               .web(true)
               .build()
               .run(args);
    }
}