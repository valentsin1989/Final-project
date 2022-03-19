package com.gmail.bsbgroup6.service.impl;

import com.gmail.bsbgroup6.repository.EmployeeDetailsRepository;
import com.gmail.bsbgroup6.repository.EmployeeRepository;
import com.gmail.bsbgroup6.repository.model.Employee;
import com.gmail.bsbgroup6.repository.model.EmployeeDetails;
import com.gmail.bsbgroup6.repository.model.Pagination;
import com.gmail.bsbgroup6.service.EmployeeService;
import com.gmail.bsbgroup6.service.converter.EmployeeConverter;
import com.gmail.bsbgroup6.service.exception.ServiceException;
import com.gmail.bsbgroup6.service.model.AddEmployeeDTO;
import com.gmail.bsbgroup6.service.model.AddedEmployeeDTO;
import com.gmail.bsbgroup6.service.model.EmployeeDTO;
import com.gmail.bsbgroup6.service.model.PaginationEmployeeDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private static final int DEFAULT_COUNT_OF_ENTITIES_PER_PAGE = 10;
    private final EmployeeRepository employeeRepository;
    private final EmployeeDetailsRepository employeeDetailsRepository;
    private final EmployeeConverter employeeConverter;

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
}
