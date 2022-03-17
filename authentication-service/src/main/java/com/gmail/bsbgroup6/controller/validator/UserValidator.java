package com.gmail.bsbgroup6.controller.validator;

import com.gmail.bsbgroup6.service.UserService;
import com.gmail.bsbgroup6.service.model.LoginDTO;
import com.gmail.bsbgroup6.service.model.UserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class UserValidator {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Long validationUser(LoginDTO loginDTO) {
        String password = loginDTO.getPassword();
        UserDTO userDTO = userService.getUser(loginDTO);
        if (userDTO != null) {
            Long id = userDTO.getId();
            String encryptedPassword = userDTO.getPassword();
            boolean isValid = passwordEncoder.matches(password, encryptedPassword);
            if (!isValid) {
                userService.addLoginFailed(loginDTO);
                return null;
            }
            return id;
        }
        return null;
    }
}
