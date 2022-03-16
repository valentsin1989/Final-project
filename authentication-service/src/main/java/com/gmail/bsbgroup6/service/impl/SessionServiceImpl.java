package com.gmail.bsbgroup6.service.impl;

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

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    @Override
    @Transactional
    public String addSessionByUserId(Long id) {
        String token = jwtUtils.generateJwtToken();
        String dateString = getDateNowInStringFormat();
        User user = userRepository.findUserById(id).orElse(null);
        if (user != null) {
            user.setLoginDate(dateString);
            user.setLoginFailed(0);
            Session session = new Session();
            session.setSessionId(token);
            session.setCreatedDate(dateString);
            user.addSession(session);
            return token;
        }
        return null;
    }

    @Override
    @Transactional
    public String updateSessionByToken(String token) {
        Session session = sessionRepository.findByToken(token).orElse(null);
        if (session != null) {
            String dateString = getDateNowInStringFormat();
            session.setClosedDate(dateString);
            User user = session.getUser();
            if(user != null){
                Session newSession = new Session();
                newSession.setSessionId(token);
                newSession.setCreatedDate(dateString);
                user.addSession(newSession);
                return token;
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
                    .filter(session -> session.getClosedDate()==null)
                    .forEach(session -> session.setClosedDate(dateString));
            return username;
        }
        return null;
    }

    @Override
    public boolean isActiveSession(String jwtToken) {
        Session session = sessionRepository.findByToken(jwtToken).orElse(null);
        if (session != null) {
            String closedDate = session.getClosedDate();
            if(closedDate == null){
                return true;
            }
        }
        return false;
    }

    private String getDateNowInStringFormat() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh.mm dd.MM.yyyy");
        String dateString = dateTimeFormatter.format(localDateTime);
        return dateString;
    }
}
