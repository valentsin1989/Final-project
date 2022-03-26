package com.gmail.bsbgroup6.repository.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfiguration {

    @Bean(name = "redisTemplate")
    public StringRedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate empTemplate = new StringRedisTemplate();
        empTemplate.setConnectionFactory(redisConnectionFactory);
        return empTemplate;
    }
}
