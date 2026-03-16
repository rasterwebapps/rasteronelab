package com.rasteronelab.lis.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rasteronelab.lis.notification", "com.rasteronelab.lis.core"})
public class LisNotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(LisNotificationApplication.class, args);
    }
}
