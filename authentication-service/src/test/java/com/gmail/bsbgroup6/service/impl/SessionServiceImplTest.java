package com.gmail.bsbgroup6.service.impl;

import com.gmail.bsbgroup6.repository.RedisRepository;
import com.gmail.bsbgroup6.repository.SessionRepository;
import com.gmail.bsbgroup6.repository.UserRepository;
import com.gmail.bsbgroup6.repository.model.Session;
import com.gmail.bsbgroup6.repository.model.User;
import com.gmail.bsbgroup6.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionServiceImplTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private RedisRepository redisRepository;

    @InjectMocks
    private SessionServiceImpl sessionService;


    @Test
    void shouldReturnNullWhenAddSessionByInvalidUserId() {
        Long userId = 1L;
        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());
        assertNull(sessionService.addSessionByUserId(userId));
    }

    @Test
    void shouldReturnTokenWhenAddSessionByValidUserId() {
        String token = "token";
        Long userId = 1L;
        String username = "test";
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setListSession(new HashSet<>());
        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(jwtUtils.generateJwtToken(username)).thenReturn(token);
        assertEquals(token, sessionService.addSessionByUserId(userId));
    }

    @Test
    void shouldReturnNewTokenWhenUpdateSessionByTokenWhichNull() {
        assertNull(sessionService.updateSessionByToken(null));
    }

    @Test
    void shouldReturnNewTokenWhenUpdateSessionByInvalidToken() {
        String token = "token";
        when(jwtUtils.validateJwtToken(token)).thenReturn(false);
        assertNull(sessionService.updateSessionByToken(token));
    }

    @Test
    void shouldReturnNewTokenWhenUpdateSessionByValidToken() {
        String token = "token";
        String newToken = "newtoken";
        String username = "username";
        User user = new User();
        user.setUsername(username);
        user.setListSession(new HashSet<>());
        Session session = new Session();
        session.setUser(user);
        when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        when(sessionRepository.findByToken(token)).thenReturn(Optional.of(session));
        when(jwtUtils.generateJwtToken(username)).thenReturn(newToken);
        assertEquals(newToken, sessionService.updateSessionByToken(token));
    }

    @Test
    void shouldReturnUsernameWhenCloseAllSessionsByInvalidUsername() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertNull(sessionService.closeAllSessionsByUsername(username));
    }

    @Test
    void shouldReturnUsernameWhenCloseAllSessionsByValidUsername() {
        String username = "test";
        User user = new User();
        user.setListSession(new HashSet<>());
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        assertEquals(username, sessionService.closeAllSessionsByUsername(username));
    }
}