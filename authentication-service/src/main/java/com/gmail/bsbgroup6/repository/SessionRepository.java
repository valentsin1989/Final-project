package com.gmail.bsbgroup6.repository;

import com.gmail.bsbgroup6.repository.model.Session;

import java.util.Optional;

public interface SessionRepository extends GenericRepository<Long, Session> {
    Optional<Session> findByToken(String token);
}
