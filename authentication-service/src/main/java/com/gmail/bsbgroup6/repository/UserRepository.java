package com.gmail.bsbgroup6.repository;

import com.gmail.bsbgroup6.repository.model.User;

import java.util.Optional;

public interface UserRepository extends GenericRepository<Long, User> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUserMail(String userMail);

    Optional<User> findUserById(Long id);

    Optional<User> findUserByNameOrMail(String username, String userMail);
}
