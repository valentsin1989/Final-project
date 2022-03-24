package com.gmail.bsbgroup6.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("${feign.service.auth}")
public interface AuthServiceRepository {

    @PostMapping(value = "/api/auth/session", consumes = MediaType.APPLICATION_JSON_VALUE)
    String getStatusToken(@RequestHeader("Authorization") String token);
}
