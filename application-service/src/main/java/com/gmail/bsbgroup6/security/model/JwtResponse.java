package com.gmail.bsbgroup6.security.model;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private final String jwt;
    private final String userName;
    private final List<String> roles;
}
