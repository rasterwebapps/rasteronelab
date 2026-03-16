package com.rasteronelab.lis.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rasteronelab.lis.admin", "com.rasteronelab.lis.core"})
public class LisAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(LisAdminApplication.class, args);
    }
}
