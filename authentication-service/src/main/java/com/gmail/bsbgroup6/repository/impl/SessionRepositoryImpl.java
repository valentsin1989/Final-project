package com.gmail.bsbgroup6.repository.impl;

import com.gmail.bsbgroup6.repository.SessionRepository;
import com.gmail.bsbgroup6.repository.model.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Optional;

@Repository
public class SessionRepositoryImpl extends GenericRepositoryImpl<Long, Session> implements SessionRepository {

    @Override
    public Optional<Session> findByToken(String token) {
        String queryString = "select u from Session as u where u.sessionId=:token";
        Query query = em.createQuery(queryString);
        query.setParameter("token", token);
        Session session;
        try {
            session = (Session) query.getSingleResult();
            return Optional.of(session);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
