package com.gmail.bsbgroup6.repository.impl;

import com.gmail.bsbgroup6.repository.LegalEntityDetailsRepository;
import com.gmail.bsbgroup6.repository.model.LegalEntityDetails;
import org.springframework.stereotype.Repository;

@Repository
public class LegalEntityDetailsRepositoryImpl extends GenericRepositoryImpl<Long, LegalEntityDetails>
        implements LegalEntityDetailsRepository {

    @Override
    public void add(LegalEntityDetails entity) {

    }
}
