package com.rasteronelab.lis.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rasteronelab.lis.inventory", "com.rasteronelab.lis.core"})
public class LisInventoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(LisInventoryApplication.class, args);
    }
}
