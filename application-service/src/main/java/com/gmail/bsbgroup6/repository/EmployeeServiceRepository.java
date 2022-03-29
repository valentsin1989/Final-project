package com.gmail.bsbgroup6.repository;

import com.gmail.bsbgroup6.service.model.EmployeeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "${feign.service.employee}", primary = false)
public interface EmployeeServiceRepository {

    @GetMapping(value = "/api/employees/{EmployeeId}")
    EmployeeDTO getEmployeeById(
            @PathVariable Long EmployeeId,
            @RequestHeader(value = "Authorization") String token
    );
}
