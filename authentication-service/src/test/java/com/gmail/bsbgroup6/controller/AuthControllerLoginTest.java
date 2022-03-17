package com.gmail.bsbgroup6.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.bsbgroup6.controller.validator.UserValidator;
import com.gmail.bsbgroup6.service.SessionService;
import com.gmail.bsbgroup6.service.UserService;
import com.gmail.bsbgroup6.service.model.LoginDTO;
import com.gmail.bsbgroup6.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class)
class AuthControllerLoginTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserService userService;
    @MockBean
    private SessionService sessionService;
    @MockBean
    private UserValidator userValidator;
    @MockBean
    private JwtUtils jwtUtils;

    @Test
    void shouldReturn400WhenLoginDTOWithAllFields() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testname");
        loginDTO.setUsermail("allen@example.com");
        loginDTO.setPassword("testPass123");
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest()).andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(actualResponse.contains("Either username or password must be entered"));
    }

    @Test
    void shouldReturn200WhenLoginWithValidUsername() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testname");
        loginDTO.setPassword("testPass123");
        when(userValidator.validationUser(loginDTO)).thenReturn(1L);
        when(sessionService.addSessionByUserId(1L)).thenReturn("token");
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(actualResponse.contains("\"sessionId\":\"token\""));
    }

    @Test
    void shouldReturn400WhenLoginWithoutValidUsername() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testname");
        loginDTO.setPassword("testPass123");
        when(userValidator.validationUser(loginDTO)).thenReturn(null);
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest()).andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(actualResponse.contains("Username or password not valid"));
    }

    @Test
    void shouldReturn200WhenLoginWithValidUserMail() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsermail("allen@example.com");
        loginDTO.setPassword("testPass123");
        when(userValidator.validationUser(loginDTO)).thenReturn(1L);
        when(sessionService.addSessionByUserId(1L)).thenReturn("token");
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(actualResponse.contains("\"sessionId\":\"token\""));
    }

    @Test
    void shouldReturn400WhenLoginWithoutValidUserMail() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsermail("allen@example.com");
        loginDTO.setPassword("testPass123");
        when(userValidator.validationUser(loginDTO)).thenReturn(null);
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest()).andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(actualResponse.contains("Username or password not valid"));
    }

    @Test
    void shouldReturn400WhenWeLoginWithPassword() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(null);
        loginDTO.setUsermail(null);
        loginDTO.setPassword("testPass123");
        when(userValidator.validationUser(loginDTO)).thenReturn(null);
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeLoginWithoutPassword() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testname");
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest()).andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(actualResponse.contains("Login or password not valid."));
    }

    @Test
    void shouldReturn400WhenWeLoginWithoutLoginDTO() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn415WhenWeLoginWithXmlContentType() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturn404WhenInvalidURL() throws Exception {
        mockMvc.perform(post("/api/auth/login/123")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn405WhenInvalidMethod() throws Exception {
        mockMvc.perform(get("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isMethodNotAllowed());
    }
}
