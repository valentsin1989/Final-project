package com.gmail.bsbgroup6.service;

import com.gmail.bsbgroup6.service.model.AddEmployeeDTO;
import com.gmail.bsbgroup6.service.model.AddedEmployeeDTO;

public interface EmployeeService {

    AddedEmployeeDTO add(AddEmployeeDTO employeeDTO, String token);
}
