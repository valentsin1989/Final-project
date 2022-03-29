package com.gmail.bsbgroup6.controller;

import com.gmail.bsbgroup6.controller.validator.UserValidator;
import com.gmail.bsbgroup6.service.SessionService;
import com.gmail.bsbgroup6.service.UserService;
import com.gmail.bsbgroup6.service.model.AddUserDTO;
import com.gmail.bsbgroup6.service.model.AddedUserDTO;
import com.gmail.bsbgroup6.service.model.LoginDTO;
import com.gmail.bsbgroup6.service.model.LogoutDTO;
import com.gmail.bsbgroup6.util.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final SessionService sessionService;
    private final JwtUtils jwtUtils;
    private final UserValidator userValidator;

    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registerUser(@Validated @RequestBody AddUserDTO addUserDTO) {
        AddedUserDTO addedUser = userService.addUser(addUserDTO);
        if (addedUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(addedUser);
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "User is not created."));
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> authenticateUser(
            @Validated @RequestBody LoginDTO loginDTO,
            @RequestHeader HttpHeaders headers
    ) {
        String jwtToken = jwtUtils.parseJwtFromHeaders(headers);
        String newJwtToken = sessionService.updateSessionByToken(jwtToken);
        if (newJwtToken != null) {
            return ResponseEntity.ok(Map.of("sessionId", newJwtToken));
        } else {
            return addSessionForUserByLogin(loginDTO);
        }
    }

    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deAuthenticateUser(@Validated @RequestBody LogoutDTO logoutDTO) {
        String username = logoutDTO.getUsername();
        String usernameWithClosedSessions = sessionService.closeAllSessionsByUsername(username);
        if (usernameWithClosedSessions == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Sessions not found."));
        }
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<Object> addSessionForUserByLogin(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String userMail = loginDTO.getUsermail();
        if (username != null && userMail != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Either username or email must be entered"));
        }
        Long userId = userValidator.validationUser(loginDTO);
        if (userId != null) {
            String token = sessionService.addSessionByUserId(userId);
            return ResponseEntity.ok(Map.of("sessionId", token));
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Username or password not valid"));
        }
    }
}
