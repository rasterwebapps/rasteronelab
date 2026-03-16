package com.rasteronelab.lis.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rasteronelab.lis.order", "com.rasteronelab.lis.core"})
public class LisOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(LisOrderApplication.class, args);
    }
}
