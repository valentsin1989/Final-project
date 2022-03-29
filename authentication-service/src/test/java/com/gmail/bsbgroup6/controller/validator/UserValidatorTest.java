package com.gmail.bsbgroup6.controller.validator;

import com.gmail.bsbgroup6.repository.model.User;
import com.gmail.bsbgroup6.service.UserService;
import com.gmail.bsbgroup6.service.model.LoginDTO;
import com.gmail.bsbgroup6.service.model.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserValidator userValidator;

    @Test
    void shouldReturnNullWhenValidationUserIfNotExist() {
        LoginDTO loginDTO = new LoginDTO();
        when(userService.getUser(loginDTO)).thenReturn(null);
        assertNull(userValidator.validationUser(loginDTO));
    }

    @Test
    void shouldReturnNullWhenValidationUserIfPasswordNotMatches() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("name");
        loginDTO.setPassword("password");
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setPassword(passwordEncoder.encode("pass"));
        when(userService.getUser(loginDTO)).thenReturn(userDTO);
        assertNull(userValidator.validationUser(loginDTO));
    }

    @Test
    void shouldReturnIdWhenValidationUserIfUserExistAndPasswordMatches() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("name");
        loginDTO.setPassword("password");
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setPassword(passwordEncoder.encode("password"));
        when(userService.getUser(loginDTO)).thenReturn(userDTO);
        assertEquals(userDTO.getId(), userValidator.validationUser(loginDTO));
    }
}