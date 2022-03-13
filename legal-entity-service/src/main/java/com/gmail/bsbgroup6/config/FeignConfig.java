package com.gmail.bsbgroup6.config;

import feign.RequestInterceptor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableFeignClients(basePackages = "com.gmail.bsbgroup6.repository")
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("Accept", "*/*");
           //requestTemplate.header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE2NDcwODk0OTIsImV4cCI6MTY0NzM0ODY5Mn0.xRQuIaSpuU-sU85yp_0AKBomlaOtu-3eBD2kUyAdfsnAznUOekURrexKoygJxu8vwe0nw-y8g7GcjdYJvJnkNA");
        };
    }
}


