package com.gmail.bsbgroup6.repository;

import com.gmail.bsbgroup6.repository.model.Employee;
import com.gmail.bsbgroup6.repository.model.Pagination;

import java.util.List;

public interface EmployeeRepository extends GenericRepository<Long, Employee> {

    List<Employee> findByPagination(Pagination pagination);

    List<Employee> findByFullName(String name);
}
