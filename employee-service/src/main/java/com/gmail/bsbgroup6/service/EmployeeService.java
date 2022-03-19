package com.gmail.bsbgroup6.service;

import com.gmail.bsbgroup6.service.model.AddEmployeeDTO;
import com.gmail.bsbgroup6.service.model.AddedEmployeeDTO;
import com.gmail.bsbgroup6.service.model.EmployeeDTO;
import com.gmail.bsbgroup6.service.model.PaginationEmployeeDTO;

import java.util.List;

public interface EmployeeService {

    AddedEmployeeDTO add(AddEmployeeDTO employeeDTO, String token);

    List<EmployeeDTO> getByPagination(PaginationEmployeeDTO employeeDTO, String token);

    EmployeeDTO getById(Long id, String token);
}
