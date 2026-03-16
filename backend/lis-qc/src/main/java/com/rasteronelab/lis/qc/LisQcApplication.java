package com.rasteronelab.lis.qc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rasteronelab.lis.qc", "com.rasteronelab.lis.core"})
public class LisQcApplication {
    public static void main(String[] args) {
        SpringApplication.run(LisQcApplication.class, args);
    }
}
