package com.gmail.bsbgroup6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class EmployeeApp {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeApp.class, args);
    }
}
