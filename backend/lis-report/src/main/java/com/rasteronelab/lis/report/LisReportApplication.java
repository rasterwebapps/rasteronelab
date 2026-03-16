package com.rasteronelab.lis.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rasteronelab.lis.report", "com.rasteronelab.lis.core"})
public class LisReportApplication {
    public static void main(String[] args) {
        SpringApplication.run(LisReportApplication.class, args);
    }
}
