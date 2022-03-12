package com.gmail.bsbgroup6.service;

public interface SessionService {

    String addSessionByUserId(Long userId);

    String updateSessionByToken(String token);

    String closeAllSessionsByUsername(String username);
}
