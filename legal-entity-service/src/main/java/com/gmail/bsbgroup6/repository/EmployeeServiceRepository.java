package com.gmail.bsbgroup6.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${feign.name}", url = "${feign.url}")
public interface EmployeeServiceRepository {

        @PostMapping(value = "/api/auth/session", consumes = "application/json")
        String getStatusToken(@RequestHeader("Authorization") String token);
}
