package com.gmail.bsbgroup6.repository;

import com.gmail.bsbgroup6.repository.model.LegalEntity;

import java.util.Optional;

public interface LegalEntityRepository extends GenericRepository<Long, LegalEntity> {

    Optional<LegalEntity> findByName(String name);

    Optional<LegalEntity> findByUnp(Integer unp);

    Optional<LegalEntity> findByIbanByByn(String ibanByByn);
}
