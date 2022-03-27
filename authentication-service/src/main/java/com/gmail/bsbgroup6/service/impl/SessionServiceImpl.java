package com.gmail.bsbgroup6.service.impl;

import com.gmail.bsbgroup6.repository.RedisRepository;
import com.gmail.bsbgroup6.repository.SessionRepository;
import com.gmail.bsbgroup6.repository.UserRepository;
import com.gmail.bsbgroup6.repository.model.Session;
import com.gmail.bsbgroup6.repository.model.User;
import com.gmail.bsbgroup6.service.SessionService;
import com.gmail.bsbgroup6.util.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
@AllArgsConstructor
public class SessionServiceImpl implements SessionService {

    public static final String DATE_PATTERN = "hh.mm dd.MM.yyyy";
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final RedisRepository redisRepository;

    @Override
    @Transactional
    public String addSessionByUserId(Long id) {
        User user = userRepository.findUserById(id).orElse(null);
        if (user != null) {
            String dateString = getDateNowInStringFormat();
            user.setLoginDate(dateString);
            user.setLoginFailed(0);
            String token = jwtUtils.generateJwtToken();
            Session session = new Session();
            session.setSessionId(token);
            session.setCreatedDate(dateString);
            user.addSession(session);
            redisRepository.add(token, "ENABLE");
            return token;
        }
        return null;
    }

    @Override
    @Transactional
    public String updateSessionByToken(String token) {
        if (token != null && jwtUtils.validateJwtToken(token)) {
            Session session = sessionRepository.findByToken(token).orElse(null);
            if (session != null) {
                String dateString = getDateNowInStringFormat();
                session.setClosedDate(dateString);
                User user = session.getUser();
                if (user != null) {
                    String newToken = jwtUtils.generateJwtToken();
                    Session newSession = new Session();
                    newSession.setSessionId(newToken);
                    newSession.setCreatedDate(dateString);
                    user.addSession(newSession);
                    redisRepository.delete(token);
                    redisRepository.add(newToken, "ENABLE");
                    return newToken;
                }
            }
        }
        return null;
    }

    @Override
    @Transactional
    public String closeAllSessionsByUsername(String username) {
        String dateString = getDateNowInStringFormat();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            user.setLogoutDate(dateString);
            Set<Session> sessions = user.getListSession();
            sessions.stream()
                    .filter(session -> session.getClosedDate() == null)
                    .forEach(session -> {
                        session.setClosedDate(dateString);
                        redisRepository.delete(session.getSessionId());
                    });
            return username;
        }
        return null;
    }

    private String getDateNowInStringFormat() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return dateTimeFormatter.format(localDateTime);
    }
}
