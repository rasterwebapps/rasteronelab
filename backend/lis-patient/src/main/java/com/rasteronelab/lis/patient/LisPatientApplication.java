package com.rasteronelab.lis.patient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rasteronelab.lis.patient", "com.rasteronelab.lis.core"})
public class LisPatientApplication {
    public static void main(String[] args) {
        SpringApplication.run(LisPatientApplication.class, args);
    }
}
