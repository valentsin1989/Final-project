package com.gmail.bsbgroup6.service.impl;

import com.gmail.bsbgroup6.repository.RedisRepository;
import com.gmail.bsbgroup6.repository.UserRepository;
import com.gmail.bsbgroup6.service.model.AddUserDTO;
import com.gmail.bsbgroup6.service.model.AddedUserDTO;
import com.gmail.bsbgroup6.service.model.UserStatusEnum;
import com.gmail.bsbgroup6.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static reactor.core.publisher.Mono.when;

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
    void shouldReturnAddedUserDTOWhenAddUser() {

    }

    @Test
    void getUser() {

    }

    @Test
    void addLoginFailed() {
    }

    @Test
    void getByUsername() {
    }

    @Test
    void getByUserMail() {
    }

    @Test
    void isBlockedUser() {
    }
}