package com.rasteronelab.lis.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rasteronelab.lis.auth", "com.rasteronelab.lis.core"})
public class LisAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(LisAuthApplication.class, args);
    }
}
