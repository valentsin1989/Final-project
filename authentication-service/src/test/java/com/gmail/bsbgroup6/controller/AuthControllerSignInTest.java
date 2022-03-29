package com.gmail.bsbgroup6.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.bsbgroup6.controller.validator.UserValidator;
import com.gmail.bsbgroup6.service.SessionService;
import com.gmail.bsbgroup6.service.UserService;
import com.gmail.bsbgroup6.service.model.AddUserDTO;
import com.gmail.bsbgroup6.service.model.AddedUserDTO;
import com.gmail.bsbgroup6.service.model.UserDTO;
import com.gmail.bsbgroup6.service.model.UserStatusEnum;
import com.gmail.bsbgroup6.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class)
class AuthControllerSignInTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private SessionService sessionService;
    @MockBean
    private UserValidator userValidator;
    @MockBean
    private JwtUtils jwtUtils;

    @Test
    void shouldReturn201WhenValidUserInput() throws Exception {
        AddUserDTO addUserDTO = new AddUserDTO();
        addUserDTO.setUsername("testname");
        addUserDTO.setPassword("testPass123");
        addUserDTO.setUsermail("allen@example.com");
        addUserDTO.setFirstName("ТестовоеИмя");
        when(userService.getByUsername(addUserDTO.getUsername())).thenReturn(null);
        when(userService.getByUserMail(addUserDTO.getUsermail())).thenReturn(null);
        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(UserStatusEnum.ACTIVE);
        when(userService.addUser(addUserDTO)).thenReturn(addedUserDTO);
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addUserDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldMapsToBusinessModelWhenValidUserInput() throws Exception {
        AddUserDTO addUserDTO = new AddUserDTO();
        addUserDTO.setUsername("testname");
        addUserDTO.setPassword("testPass123");
        addUserDTO.setUsermail("allen@example.com");
        addUserDTO.setFirstName("ТестовоеИмя");
        when(userService.getByUsername(addUserDTO.getUsername())).thenReturn(null);
        when(userService.getByUserMail(addUserDTO.getUsermail())).thenReturn(null);
        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(UserStatusEnum.ACTIVE);
        when(userService.addUser(addUserDTO)).thenReturn(addedUserDTO);
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addUserDTO)))
                .andExpect(status().isCreated())
                .andReturn();
        verify(userService, times(1)).addUser(addUserDTO);
        String actualResponse = mvcResult.getResponse().getContentAsString();
        AddedUserDTO addedUserDTOFromResponse = objectMapper.readValue(actualResponse, AddedUserDTO.class);
        Assertions.assertEquals(addedUserDTOFromResponse.getUserId(), addedUserDTOFromResponse.getUserId());
    }

    @Test
    void shouldReturnUserWhenWeRegisterUserWithValidInput() throws Exception {
        AddUserDTO addUserDTO = new AddUserDTO();
        addUserDTO.setUsername("testname");
        addUserDTO.setPassword("testPass123");
        addUserDTO.setUsermail("allen@example.com");
        addUserDTO.setFirstName("ТестовоеИмя");
        when(userService.getByUsername(addUserDTO.getUsername())).thenReturn(null);
        when(userService.getByUserMail(addUserDTO.getUsermail())).thenReturn(null);
        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(UserStatusEnum.ACTIVE);
        when(userService.addUser(addUserDTO)).thenReturn(addedUserDTO);
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addUserDTO)))
                .andExpect(status().isCreated())
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(addedUserDTO));
    }

    @Test
    void shouldReturn400whenUsernameAlreadyExist() throws Exception {
        AddUserDTO addUserDTO = new AddUserDTO();
        addUserDTO.setUsername("testname");
        addUserDTO.setPassword("testPass123");
        addUserDTO.setUsermail("allen@example.com");
        addUserDTO.setFirstName("ТестовоеИмя");
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername(addUserDTO.getUsername());
        userDTO.setPassword(addUserDTO.getPassword());
        when(userService.getByUsername(addUserDTO.getUsername())).thenReturn(userDTO);
        when(userService.getByUserMail(addUserDTO.getUsermail())).thenReturn(null);
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400whenUserMailAlreadyExist() throws Exception {
        AddUserDTO addUserDTO = new AddUserDTO();
        addUserDTO.setUsername("testname");
        addUserDTO.setPassword("testPass123");
        addUserDTO.setUsermail("allen@example.com");
        addUserDTO.setFirstName("ТестовоеИмя");
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername(addUserDTO.getUsername());
        userDTO.setPassword(addUserDTO.getPassword());
        when(userService.getByUsername(addUserDTO.getUsername())).thenReturn(null);
        when(userService.getByUserMail(addUserDTO.getUsermail())).thenReturn(userDTO);
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignIn() throws Exception {
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn415WhenWeSignInWithXmlContentType() throws Exception {
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturn404WhenInvalidURL() throws Exception {
        mockMvc.perform(post("/api/auth/signin/123")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn405WhenInvalidMethod() throws Exception {
        mockMvc.perform(get("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isMethodNotAllowed());
    }
}