package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class leyouRegistry {
    public static void main(String[] args) {
        SpringApplication.run(leyouRegistry.class);
    }
}
