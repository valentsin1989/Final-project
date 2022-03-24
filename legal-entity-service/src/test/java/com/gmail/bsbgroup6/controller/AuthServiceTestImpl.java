package com.gmail.bsbgroup6.controller;

import com.gmail.bsbgroup6.repository.AuthenticationServiceRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class AuthServiceTestImpl implements AuthenticationServiceRepository {
    @Override
    public String getStatusToken(String token) {
        return "ENABLE";
    }
}
