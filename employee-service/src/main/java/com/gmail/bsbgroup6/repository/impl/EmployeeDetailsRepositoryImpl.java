package com.gmail.bsbgroup6.repository.impl;

import com.gmail.bsbgroup6.repository.EmployeeDetailsRepository;
import com.gmail.bsbgroup6.repository.model.EmployeeDetails;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeDetailsRepositoryImpl extends GenericRepositoryImpl<Long, EmployeeDetails>
        implements EmployeeDetailsRepository {

    @Override
    public void add(EmployeeDetails entity) {

    }
}
