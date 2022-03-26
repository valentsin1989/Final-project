package com.gmail.bsbgroup6.repository;

public interface RedisRepository {
    void add(String token, String status);
    void delete(String token);
}
