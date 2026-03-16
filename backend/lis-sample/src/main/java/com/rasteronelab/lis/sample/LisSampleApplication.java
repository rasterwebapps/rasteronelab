package com.rasteronelab.lis.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rasteronelab.lis.sample", "com.rasteronelab.lis.core"})
public class LisSampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(LisSampleApplication.class, args);
    }
}
