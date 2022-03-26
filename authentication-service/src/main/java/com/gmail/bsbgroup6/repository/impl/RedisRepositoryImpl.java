package com.gmail.bsbgroup6.repository.impl;

import com.gmail.bsbgroup6.repository.RedisRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {

    private StringRedisTemplate redisTemplate;

    @Override
    public void add(String token, String status) {
        redisTemplate.opsForList().leftPush(token, status);
    }

    @Override
    public void delete(String token) {
        redisTemplate.delete(token);
    }
}
