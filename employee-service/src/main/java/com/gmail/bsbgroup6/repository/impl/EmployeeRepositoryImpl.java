package com.gmail.bsbgroup6.repository.impl;

import com.gmail.bsbgroup6.repository.EmployeeRepository;
import com.gmail.bsbgroup6.repository.model.Employee;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepositoryImpl extends GenericRepositoryImpl<Long, Employee>
        implements EmployeeRepository {

}
