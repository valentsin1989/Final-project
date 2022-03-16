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

    @PostMapping(value = "/signin", consumes = "application/json")
    public ResponseEntity<Object> registerUser(@Validated @RequestBody AddUserDTO addUserDTO) {
        AddedUserDTO addedUser = userService.addUser(addUserDTO);
        if(addedUser!=null){
            return ResponseEntity.status(201).body(addedUser);
        }
        return ResponseEntity
                .status(400)
                .body(Map.of("message", "User is not created."));
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<Object> authenticateUser(@Validated @RequestBody LoginDTO loginDTO, @RequestHeader HttpHeaders headers) {
        String jwtToken = jwtUtils.parseJwtFromHeaders(headers);
        if (jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
            String newJwtToken = sessionService.updateSessionByToken(jwtToken);
            if (newJwtToken != null){
                return ResponseEntity.ok(newJwtToken);
            }
        }
        String username = loginDTO.getUsername();
        String userMail = loginDTO.getUserMail();
        String password = loginDTO.getPassword();
        if (username != null && userMail != null) {
            return ResponseEntity
                    .status(400)
                    .body(Map.of("message", "Either username or password must be entered"));
        } else if (username != null){
            Long userId = userValidator.validationUserByUsername(username, password);
            if(userId!=null){
                String token = sessionService.addSessionByUserId(userId);
                return ResponseEntity.ok(Map.of("sessionId", token));
            } else {
                return ResponseEntity
                        .status(400)
                        .body(Map.of("message", "Username or password not valid"));
            }
        }else if (userMail != null) {
            Long userId = userValidator.validationUserByUserMail(userMail, password);
            if(userId!=null){
                String token = sessionService.addSessionByUserId(userId);
                return ResponseEntity.ok(Map.of("sessionId", token));
            } else {
                return ResponseEntity
                        .status(400)
                        .body(Map.of("message", "Mail or password not valid"));
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(value = "/logout", consumes = "application/json")
    public ResponseEntity<Object> deAuthenticateUser(@Validated @RequestBody LogoutDTO logoutDTO) {
        String username = logoutDTO.getUsername();
        String usernameWithClosedSessions = sessionService.closeAllSessionsByUsername(username);
        if(usernameWithClosedSessions==null){
            return ResponseEntity
                    .status(400)
                    .body(Map.of("message", "Sessions not found."));
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/session", consumes = "application/json")
    public ResponseEntity<Object> isActiveSession(@RequestHeader HttpHeaders headers) {
        String jwtToken = jwtUtils.parseJwtFromHeaders(headers);
        if (jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
            boolean isActiveSession = sessionService.isActiveSession(jwtToken);
            if(isActiveSession){
                return ResponseEntity.ok().body("ENABLE");
            }
        }
        return ResponseEntity.ok().body("DISABLE");
    }
}
