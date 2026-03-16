package com.rasteronelab.lis.instrument;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rasteronelab.lis.instrument", "com.rasteronelab.lis.core"})
public class LisInstrumentApplication {
    public static void main(String[] args) {
        SpringApplication.run(LisInstrumentApplication.class, args);
    }
}
