package com.gmail.bsbgroup6.service;

import com.gmail.bsbgroup6.service.model.AddEmployeeDTO;
import com.gmail.bsbgroup6.service.model.AddedEmployeeDTO;
import com.gmail.bsbgroup6.service.model.EmployeeDTO;
import com.gmail.bsbgroup6.service.model.GetEmployeeDTO;
import com.gmail.bsbgroup6.service.model.PaginationEmployeeDTO;
import com.gmail.bsbgroup6.service.model.SearchEmployeeDTO;

import java.util.List;

public interface EmployeeService {

    AddedEmployeeDTO add(AddEmployeeDTO employeeDTO, String token);

    List<EmployeeDTO> getByPagination(PaginationEmployeeDTO employeeDTO, String token);

    EmployeeDTO getById(Long id, String token);

    List<GetEmployeeDTO> getByParameters(SearchEmployeeDTO searchEmployeeDTO, String token);

    AddedEmployeeDTO getByFullName(String fullName);

    AddedEmployeeDTO getByPersonIbanByn(String personIbanByn);

    AddedEmployeeDTO getByPersonIbanCurrency(String personIbanCurrency);
}
