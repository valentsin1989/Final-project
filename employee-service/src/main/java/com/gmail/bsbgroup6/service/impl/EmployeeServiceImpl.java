package com.gmail.bsbgroup6.service.impl;

import com.gmail.bsbgroup6.repository.EmployeeRepository;
import com.gmail.bsbgroup6.repository.model.Employee;
import com.gmail.bsbgroup6.service.EmployeeService;
import com.gmail.bsbgroup6.service.converter.EmployeeConverter;
import com.gmail.bsbgroup6.service.exception.ServiceException;
import com.gmail.bsbgroup6.service.model.AddEmployeeDTO;
import com.gmail.bsbgroup6.service.model.AddedEmployeeDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeConverter employeeConverter;

    @Override
    public AddedEmployeeDTO add(AddEmployeeDTO employeeDTO) {
        Employee employee = employeeConverter.convertToEmployee(employeeDTO);
        employeeRepository.add(employee);
        Long id = employee.getId();
        if (id == null) {
            throw new ServiceException("Employee wasn't added.");
        }
        return employeeConverter.convertToAddedEmployeeDTO(employee);
    }
}
