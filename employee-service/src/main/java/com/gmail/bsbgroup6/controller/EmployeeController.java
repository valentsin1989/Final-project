package com.gmail.bsbgroup6.controller;

import com.gmail.bsbgroup6.service.EmployeeService;
import com.gmail.bsbgroup6.service.model.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Object> addEmployee(
            @Validated @RequestBody AddEmployeeDTO employeeDTO,
            @RequestHeader(value = "Authorization") String token
    ) {
        AddedEmployeeDTO addedEmployeeDTO = employeeService.add(employeeDTO, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedEmployeeDTO);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> getEmployees(
            @RequestParam(name = "pagination", required = false) PaginationEnum pagination,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "customized_page", required = false) Integer customizedPage,
            @RequestParam(name = "Name_Legal", required = false) String legalEntityName,
            @RequestParam(name = "UNP", required = false) Integer unp,
            @RequestParam(name = "Full_Name_Individual", required = false) String fullName,
            @RequestHeader(value = "Authorization") String token
    ) {
        if (pagination != null) {
            PaginationEmployeeDTO employeeDTO = new PaginationEmployeeDTO();
            employeeDTO.setPagination(pagination);
            employeeDTO.setPage(page);
            employeeDTO.setCustomizedPage(customizedPage);
            List<EmployeeDTO> employees = employeeService.getByPagination(employeeDTO, token);
            if (employees.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Сотрудники не найдены");
            }
            return ResponseEntity.status(HttpStatus.OK).body(employees);
        }

        SearchEmployeeDTO searchEmployeeDTO = new SearchEmployeeDTO();
        searchEmployeeDTO.setLegalEntityName(legalEntityName);
        searchEmployeeDTO.setUnp(unp);
        searchEmployeeDTO.setFullName(fullName);
        List<GetEmployeeDTO> employees = employeeService.getByParameters(searchEmployeeDTO, token);
        if (employees.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Сотрудник не найден, измените параметры поиска");
        }
        return ResponseEntity.status(HttpStatus.OK).body(employees);
    }

    @GetMapping(value = "/{EmployeeId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Object> getEmployee(
            @PathVariable Long EmployeeId,
            @RequestHeader(value = "Authorization") String token
    ) {
        EmployeeDTO employeeDTO = employeeService.getById(EmployeeId, token);
        if (employeeDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Сотрудника не существует");
        }
        return ResponseEntity.status(HttpStatus.OK).body(employeeDTO);
    }
}
