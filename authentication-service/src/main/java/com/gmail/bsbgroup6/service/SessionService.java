package com.gmail.bsbgroup6.service;

public interface SessionService {

    String addSessionByUserId(Long userId);

    String updateSessionByToken(String jwtToken);

    String closeAllSessionsByUsername(String username);

    boolean isActiveSession(String jwtToken);

}
