package com.gmail.bsbgroup6.controller.validator;

import com.gmail.bsbgroup6.service.EmployeeService;
import com.gmail.bsbgroup6.service.model.AddEmployeeDTO;
import com.gmail.bsbgroup6.service.model.AddedEmployeeDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class EmployeeValidator {

    private final EmployeeService employeeService;

    public boolean isEmployeeExists(AddEmployeeDTO addEmployeeDTO) {
        String fullName = addEmployeeDTO.getFullName();
        String personIbanByn = addEmployeeDTO.getPersonIbanByn();
        String personIbanCurrency = addEmployeeDTO.getPersonIbanCurrency();
        if (fullName != null) {
            AddedEmployeeDTO employeeDTO = employeeService.getByFullName(fullName);
            if (employeeDTO != null) {
                return true;
            }
        }
        if (personIbanByn != null) {
            AddedEmployeeDTO employeeDTO = employeeService.getByPersonIbanByn(personIbanByn);
            if (employeeDTO != null) {
                return true;
            }
        }
        if (personIbanCurrency != null) {
            AddedEmployeeDTO employeeDTO = employeeService.getByPersonIbanCurrency(personIbanCurrency);
            return employeeDTO != null;
        }
        return false;
    }
}
