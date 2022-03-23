package com.gmail.bsbgroup6.repository;

import com.gmail.bsbgroup6.repository.model.Employee;
import com.gmail.bsbgroup6.repository.model.Pagination;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends GenericRepository<Long, Employee> {

    List<Employee> findByPagination(Pagination pagination);

    List<Employee> findByFullName(String name);

    Optional<Employee> findByName(String fullName);

    Optional<Employee> findByPersonIbanByn(String personIbanByn);

    Optional<Employee> findByPersonIbanCurrency(String personIbanCurrency);
}
