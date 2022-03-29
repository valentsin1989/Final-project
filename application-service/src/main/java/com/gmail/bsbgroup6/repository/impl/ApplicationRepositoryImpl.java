package com.gmail.bsbgroup6.repository.impl;

import com.gmail.bsbgroup6.repository.ApplicationRepository;
import com.gmail.bsbgroup6.repository.model.Application;
import com.gmail.bsbgroup6.repository.model.Pagination;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ApplicationRepositoryImpl extends GenericRepositoryImpl<Long, Application>
        implements ApplicationRepository {

    @Override
    public Optional<Application> findByUUID(UUID uniqueNumber) {
        String queryString = "select a from Application as a where a.uniqueNumber=:uniqueNumber";
        Query query = em.createQuery(queryString);
        query.setParameter("uniqueNumber", uniqueNumber);
        Application application;
        try {
            application = (Application) query.getSingleResult();
            return Optional.of(application);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Application> findByPagination(Pagination pagination) {
        String queryString = "select a from Application as a order by a.id asc";
        int page = pagination.getPage();
        int maxResult = pagination.getMaxResult();
        Query query = em.createQuery(queryString);
        query.setFirstResult((maxResult * page) - maxResult);
        query.setMaxResults(maxResult);
        try {
            return (List<Application>) query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
}
