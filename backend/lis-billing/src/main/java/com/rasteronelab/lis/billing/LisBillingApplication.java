package com.rasteronelab.lis.billing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rasteronelab.lis.billing", "com.rasteronelab.lis.core"})
public class LisBillingApplication {
    public static void main(String[] args) {
        SpringApplication.run(LisBillingApplication.class, args);
    }
}
