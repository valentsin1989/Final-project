package com.gmail.bsbgroup6.repository.impl;

import com.gmail.bsbgroup6.repository.LegalEntityDatesRepository;
import com.gmail.bsbgroup6.repository.model.LegalEntityDates;
import org.springframework.stereotype.Repository;

@Repository
public class LegalEntityDatesRepositoryImpl extends GenericRepositoryImpl<Long, LegalEntityDates>
        implements LegalEntityDatesRepository {

    @Override
    public void add(LegalEntityDates entity) {

    }
}
