package com.gmail.bsbgroup6.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${feign.auth.name}", url = "${feign.auth.url}")
public interface AuthServiceRepository {

        @PostMapping(value = "/api/auth/session", consumes = "application/json")
        String getStatusToken(@RequestHeader("Authorization") String token);
}
