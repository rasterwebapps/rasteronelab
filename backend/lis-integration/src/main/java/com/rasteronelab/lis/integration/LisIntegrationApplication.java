package com.rasteronelab.lis.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rasteronelab.lis.integration", "com.rasteronelab.lis.core"})
public class LisIntegrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(LisIntegrationApplication.class, args);
    }
}
