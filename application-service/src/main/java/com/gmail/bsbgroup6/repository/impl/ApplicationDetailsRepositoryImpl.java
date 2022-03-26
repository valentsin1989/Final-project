package com.gmail.bsbgroup6.repository.impl;

import com.gmail.bsbgroup6.repository.ApplicationDetailsRepository;
import com.gmail.bsbgroup6.repository.model.ApplicationDetails;
import org.springframework.stereotype.Repository;

@Repository
public class ApplicationDetailsRepositoryImpl extends GenericRepositoryImpl<Long, ApplicationDetails>
        implements ApplicationDetailsRepository {

    @Override
    public void add(ApplicationDetails entity) {

    }
}
