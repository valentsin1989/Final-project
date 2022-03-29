package com.gmail.bsbgroup6.repository;

import com.gmail.bsbgroup6.repository.model.LegalEntity;
import com.gmail.bsbgroup6.repository.model.LegalSearch;
import com.gmail.bsbgroup6.repository.model.Pagination;

import java.util.List;
import java.util.Optional;

public interface LegalEntityRepository extends GenericRepository<Long, LegalEntity> {

    Optional<LegalEntity> findByName(String name);

    Optional<LegalEntity> findByUnp(String unp);

    Optional<LegalEntity> findByIbanByByn(String ibanByByn);

    List<LegalEntity> findByPagination(Pagination pagination);

    List<LegalEntity> findByParameters(LegalSearch legalSearch);
}
