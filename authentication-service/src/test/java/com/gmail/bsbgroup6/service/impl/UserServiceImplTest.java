package com.gmail.bsbgroup6.service.impl;

import com.gmail.bsbgroup6.repository.RedisRepository;
import com.gmail.bsbgroup6.repository.UserRepository;
import com.gmail.bsbgroup6.repository.model.StatusEnum;
import com.gmail.bsbgroup6.repository.model.User;
import com.gmail.bsbgroup6.service.model.AddUserDTO;
import com.gmail.bsbgroup6.service.model.LoginDTO;
import com.gmail.bsbgroup6.service.model.UserDTO;
import com.gmail.bsbgroup6.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private RedisRepository redisRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void shouldReturnAddedUserWhenAddUserNotComplete() {
        AddUserDTO addUserDTO = new AddUserDTO();
        addUserDTO.setUsername("testname");
        addUserDTO.setPassword("testPass123");
        addUserDTO.setUsermail("allen@example.com");
        addUserDTO.setFirstName("ТестовоеИмя");
        assertNull(userService.addUser(addUserDTO));
    }

    @Test
    void shouldReturnNullWhenGetUserIfUserNotExist() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testname");
        loginDTO.setUsermail("allen@example.com");
        loginDTO.setPassword("testPass123");
        when(userRepository.findUserByNameOrMail(loginDTO.getUsername(), loginDTO.getUsermail())).thenReturn(Optional.empty());
        assertNull(userService.getUser(loginDTO));
    }

    @Test
    void shouldReturnUserDTOWhenGetUserIfUserExist() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testname");
        loginDTO.setUsermail("allen@example.com");
        loginDTO.setPassword("testPass123");
        User user = new User();
        user.setId(1L);
        user.setUsername("testname");
        user.setPassword("testPass123");
        when(userRepository.findUserByNameOrMail(loginDTO.getUsername(), loginDTO.getUsermail())).thenReturn(Optional.of(user));
        assertEquals(UserDTO.class, userService.getUser(loginDTO).getClass());
    }

    @Test
    void shouldReturnTrueWhenAddLoginFailedIfUserExist() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testname");
        loginDTO.setUsermail("allen@example.com");
        User user = new User();
        user.setLoginFailed(0);
        when(userRepository.findUserByNameOrMail(loginDTO.getUsername(), loginDTO.getUsermail())).thenReturn(Optional.of(user));
        assertTrue(userService.addLoginFailed(loginDTO));
    }

    @Test
    void shouldReturnTrueWhenAddLoginFailedIfUserNotExist() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testname");
        loginDTO.setUsermail("allen@example.com");
        when(userRepository.findUserByNameOrMail(loginDTO.getUsername(), loginDTO.getUsermail())).thenReturn(Optional.empty());
        assertFalse(userService.addLoginFailed(loginDTO));
    }

    @Test
    void shouldReturnUserDTOWhenGetByUsernameWhenUserNotExist() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertNull(userService.getByUsername(username));
    }

    @Test
    void shouldReturnUserDTOWhenGetByUsernameWhenUserExist() {
        String username = "test";
        User user = new User();
        user.setId(1L);
        user.setUsername("testname");
        user.setPassword("testPass123");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        assertEquals(UserDTO.class, userService.getByUsername(username).getClass());
    }

    @Test
    void shouldReturnUserDTOWhenGetByUserMailWhenUserNotExist() {
        String usermail = "test";
        when(userRepository.findByUserMail(usermail)).thenReturn(Optional.empty());
        assertNull(userService.getByUserMail(usermail));
    }

    @Test
    void shouldReturnUserDTOWhenGetByUserMailWhenUserExist() {
        String usermail = "test";
        User user = new User();
        user.setId(1L);
        user.setUsername("testname");
        user.setPassword("testPass123");
        when(userRepository.findByUserMail(usermail)).thenReturn(Optional.of(user));
        assertEquals(UserDTO.class, userService.getByUserMail(usermail).getClass());
    }


    @Test
    void ShouldReturnTrueWhenIsBlockedUserIfUserBlocked() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testname");
        loginDTO.setUsermail("allen@example.com");
        User user = new User();
        user.setStatus(StatusEnum.DISABLE);
        when(userRepository.findUserByNameOrMail(loginDTO.getUsername(), loginDTO.getUsermail())).thenReturn(Optional.of(user));
        assertTrue(userService.isBlockedUser(loginDTO));
    }

    @Test
    void ShouldReturnTrueWhenIsBlockedUserIfUserNotBlocked() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testname");
        loginDTO.setUsermail("allen@example.com");
        User user = new User();
        user.setStatus(StatusEnum.ACTIVE);
        when(userRepository.findUserByNameOrMail(loginDTO.getUsername(), loginDTO.getUsermail())).thenReturn(Optional.of(user));
        assertFalse(userService.isBlockedUser(loginDTO));
    }
}