package com.gmail.bsbgroup6.service.impl;

import com.gmail.bsbgroup6.repository.EmployeeDetailsRepository;
import com.gmail.bsbgroup6.repository.EmployeeRepository;
import com.gmail.bsbgroup6.repository.LegalServiceRepository;
import com.gmail.bsbgroup6.repository.model.Employee;
import com.gmail.bsbgroup6.repository.model.EmployeeDetails;
import com.gmail.bsbgroup6.repository.model.Pagination;
import com.gmail.bsbgroup6.service.EmployeeService;
import com.gmail.bsbgroup6.service.converter.EmployeeConverter;
import com.gmail.bsbgroup6.service.exception.ServiceException;
import com.gmail.bsbgroup6.service.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private static final int DEFAULT_COUNT_OF_ENTITIES_PER_PAGE = 10;
    private final EmployeeRepository employeeRepository;
    private final EmployeeDetailsRepository employeeDetailsRepository;
    private final EmployeeConverter employeeConverter;
    private final LegalServiceRepository legalServiceRepository;

    @Override
    @Transactional
    public AddedEmployeeDTO add(AddEmployeeDTO employeeDTO, String token) {
        Employee employee = employeeConverter.convertToEmployee(employeeDTO, token);
        employeeRepository.add(employee);
        Long id = employee.getId();
        if (id == null) {
            throw new ServiceException("Employee wasn't added.");
        }
        Employee employeeWthId = employeeRepository.findById(id);
        EmployeeDetails employeeDetails = employeeConverter.convertToEmployeeDetails(employeeWthId);
        employeeDetailsRepository.add(employeeDetails);
        return employeeConverter.convertToAddedEmployeeDTO(employee);
    }

    @Override
    @Transactional
    public List<EmployeeDTO> getByPagination(PaginationEmployeeDTO employeeDTO, String token) {
        List<Employee> employees;
        switch (employeeDTO.getPagination()) {
            case DEFAULT: {
                Pagination pagination = new Pagination();
                pagination.setPage(employeeDTO.getPage());
                pagination.setMaxResult(DEFAULT_COUNT_OF_ENTITIES_PER_PAGE);
                employees = employeeRepository.findByPagination(pagination);
                break;
            }
            case CUSTOMED: {
                Pagination pagination = new Pagination();
                pagination.setPage(employeeDTO.getPage());
                pagination.setMaxResult(employeeDTO.getCustomizedPage());
                employees = employeeRepository.findByPagination(pagination);
                break;
            }
            default: {
                throw new ServiceException("Employees are not found.");
            }
        }
        return employeeConverter.convertToListEmployeeDTO(employees, token);
    }

    @Override
    @Transactional
    public EmployeeDTO getById(Long id, String token) {
        Employee employee = employeeRepository.findById(id);
        if (employee == null) {
            return null;
        }
        return employeeConverter.convertToEmployeeDTO(employee, token);
    }

    @Override
    @Transactional
    public List<GetEmployeeDTO> getByParameters(SearchEmployeeDTO searchEmployeeDTO, String token) {
        String fullName = searchEmployeeDTO.getFullName();
        List<Employee> employees = employeeRepository.findByFullName(fullName);
        if (employees.isEmpty()) {
            return Collections.emptyList();
        }
        String legalEntityName = searchEmployeeDTO.getLegalEntityName();
        Integer unp = searchEmployeeDTO.getUnp();
        String unpString = null;
        if (unp != null) {
            unpString = unp.toString();
        }
        List<LegalEntityDTO> legals = legalServiceRepository.getLegalByNameAndUnp(legalEntityName, unpString, token);
        List<GetEmployeeDTO> getEmployeeDTOs = new ArrayList<>();
        for (LegalEntityDTO legal : legals) {
            String legalName = legal.getName();
            getEmployeeDTOs = employees.stream()
                    .filter(employee -> employee.getLegalEntityId().equals(legal.getId()))
                    .map(employee -> employeeConverter.convertToGetEmployeeDTO(employee, legalName))
                    .collect(Collectors.toList());
        }
        return getEmployeeDTOs;
    }

    @Override
    @Transactional
    public AddedEmployeeDTO getByFullName(String fullName) {
        Employee employee = employeeRepository.findByName(fullName).orElse(null);
        if (employee == null) {
            return null;
        }
        return employeeConverter.convertToAddedEmployeeDTO(employee);
    }

    @Override
    @Transactional
    public AddedEmployeeDTO getByPersonIbanByn(String personIbanByn) {
        Employee employee = employeeRepository.findByPersonIbanByn(personIbanByn).orElse(null);
        if (employee == null) {
            return null;
        }
        return employeeConverter.convertToAddedEmployeeDTO(employee);
    }

    @Override
    @Transactional
    public AddedEmployeeDTO getByPersonIbanCurrency(String personIbanCurrency) {
        Employee employee = employeeRepository.findByPersonIbanCurrency(personIbanCurrency).orElse(null);
        if (employee == null) {
            return null;
        }
        return employeeConverter.convertToAddedEmployeeDTO(employee);
    }
}