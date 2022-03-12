package com.gmail.bsbgroup6.repository.impl;

import com.gmail.bsbgroup6.repository.UserRepository;
import com.gmail.bsbgroup6.repository.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {

    @Override
    public Optional<User> findByUsername(String username) {
        String queryString = "select u from User as u where u.username=:username";
        Query query = em.createQuery(queryString);
        query.setParameter("username", username);
        User user;
        try {
            user = (User) query.getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByUserMail(String userMail) {
        String queryString = "select u from User as u where u.mail=:mail";
        Query query = em.createQuery(queryString);
        query.setParameter("mail", userMail);
        User user;
        try {
            user = (User) query.getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findUserById(Long id) {
        String queryString = "select u from User as u where u.id=:id";
        Query query = em.createQuery(queryString);
        query.setParameter("id", id);
        User user;
        try {
            user = (User) query.getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
