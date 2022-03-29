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
    public boolean isExist(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}
