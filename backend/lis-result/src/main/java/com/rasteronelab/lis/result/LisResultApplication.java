package com.rasteronelab.lis.result;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rasteronelab.lis.result", "com.rasteronelab.lis.core"})
public class LisResultApplication {
    public static void main(String[] args) {
        SpringApplication.run(LisResultApplication.class, args);
    }
}
