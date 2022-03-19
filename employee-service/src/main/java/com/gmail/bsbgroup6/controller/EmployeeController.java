package com.gmail.bsbgroup6.controller;

import com.gmail.bsbgroup6.service.EmployeeService;
import com.gmail.bsbgroup6.service.model.AddEmployeeDTO;
import com.gmail.bsbgroup6.service.model.AddedEmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Object> addLegalEntity(
            @Validated @RequestBody AddEmployeeDTO employeeDTO,
            @RequestHeader(value = "Authorization") String token
    ) {
        AddedEmployeeDTO addedEmployeeDTO = employeeService.add(employeeDTO, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedEmployeeDTO);
    }
}
