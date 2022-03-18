package com.gmail.bsbgroup6.repository.impl;

import com.gmail.bsbgroup6.repository.LegalEntityRepository;
import com.gmail.bsbgroup6.repository.model.LegalEntity;
import com.gmail.bsbgroup6.repository.model.LegalSearch;
import com.gmail.bsbgroup6.repository.model.Pagination;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;
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

    @Override
    public List<LegalEntity> findByPagination(Pagination pagination) {
        String queryString = "select l from LegalEntity as l order by l.id asc";
        int page = pagination.getPage();
        int maxResult = pagination.getMaxResult();
        Query query = em.createQuery(queryString);
        query.setFirstResult((maxResult * page) - maxResult);
        query.setMaxResults(maxResult);
        try {
            return (List<LegalEntity>) query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<LegalEntity> findByParameters(LegalSearch legalSearch) {
        String name = legalSearch.getName();
        String unp = legalSearch.getUnp();
        String ibanByByn = legalSearch.getIbanByByn();
        StringBuilder command = new StringBuilder("select l from LegalEntity as l");
        boolean isAppendedName = appendParameter(false, name, command, " l.name like :name");
        boolean isAppendedUnp = appendParameter(isAppendedName, unp, command, " l.unp like :unp");
        boolean isAppendedIban;
        if (!isAppendedUnp) {
            isAppendedIban = appendParameter(
                    isAppendedName, ibanByByn, command, " l.ibanByByn like :ibanByByn"
            );
        } else {
            isAppendedIban = appendParameter(
                    true, ibanByByn, command, " l.ibanByByn like :ibanByByn"
            );
        }
        Query query = em.createQuery(command.toString());
        if (isAppendedName) {
            query.setParameter("name", name + "%");
        }
        if (isAppendedUnp) {
            query.setParameter("unp", unp + "%");
        }
        if (isAppendedIban) {
            query.setParameter("ibanByByn", ibanByByn + "%");
        }
        try {
            return (List<LegalEntity>) query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    private boolean appendParameter(Boolean isAppended, String field, StringBuilder command, String appendString) {
        if (field != null) {
            if (!isAppended) {
                command.append(" where");
            } else {
                command.append(" and");
            }
            command.append(appendString);
            return true;
        } else {
            return false;
        }
    }
}
