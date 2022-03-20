package com.gmail.bsbgroup6.service;

import com.gmail.bsbgroup6.service.model.*;

import java.util.List;

public interface EmployeeService {

    AddedEmployeeDTO add(AddEmployeeDTO employeeDTO, String token);

    List<EmployeeDTO> getByPagination(PaginationEmployeeDTO employeeDTO, String token);

    EmployeeDTO getById(Long id, String token);

    List<GetEmployeeDTO> getByParameters(SearchEmployeeDTO searchEmployeeDTO, String token);
}
