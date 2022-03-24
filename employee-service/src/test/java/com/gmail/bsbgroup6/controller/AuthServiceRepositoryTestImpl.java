package com.gmail.bsbgroup6.controller;


import com.gmail.bsbgroup6.repository.AuthServiceRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class AuthServiceRepositoryTestImpl implements AuthServiceRepository {
    @Override
    public String getStatusToken(String token) {
        return "ENABLE";
    }
}
