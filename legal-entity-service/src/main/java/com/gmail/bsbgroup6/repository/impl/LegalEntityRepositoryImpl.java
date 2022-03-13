package com.gmail.bsbgroup6.repository.impl;

import com.gmail.bsbgroup6.repository.LegalEntityRepository;
import com.gmail.bsbgroup6.repository.model.LegalEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Optional;

@Repository
public class LegalEntityRepositoryImpl extends GenericRepositoryImpl<Long, LegalEntity>
        implements LegalEntityRepository {

    @Override
    public Optional<LegalEntity> findByName(String name) {
        String queryString = "select l from LegalEntity as l where l.name=:name";
        Query query = em.createQuery(queryString);
        query.setParameter("name", name);
        LegalEntity legalEntity;
        try {
            legalEntity = (LegalEntity) query.getSingleResult();
            return Optional.of(legalEntity);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<LegalEntity> findByUnp(Integer unp) {
        String queryString = "select l from LegalEntity as l where l.unp=:unp";
        Query query = em.createQuery(queryString);
        query.setParameter("unp", unp);
        LegalEntity legalEntity;
        try {
            legalEntity = (LegalEntity) query.getSingleResult();
            return Optional.of(legalEntity);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<LegalEntity> findByIbanByByn(String ibanByByn) {
        String queryString = "select l from LegalEntity as l where l.ibanByByn=:ibanByByn";
        Query query = em.createQuery(queryString);
        query.setParameter("ibanByByn", ibanByByn);
        LegalEntity legalEntity;
        try {
            legalEntity = (LegalEntity) query.getSingleResult();
            return Optional.of(legalEntity);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
