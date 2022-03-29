package com.gmail.bsbgroup6.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.bsbgroup6.controller.validator.UserValidator;
import com.gmail.bsbgroup6.service.SessionService;
import com.gmail.bsbgroup6.service.UserService;
import com.gmail.bsbgroup6.service.model.LogoutDTO;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class)
class AuthControllerLogoutTest {
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
    void shouldReturn200WhenWeLogoutWithValidInput() throws Exception {
        LogoutDTO logoutDTO = new LogoutDTO();
        logoutDTO.setUsername("testname");
        logoutDTO.setSessionId("testJwt");
        when(sessionService.closeAllSessionsByUsername(logoutDTO.getUsername())).thenReturn("testname");
        mockMvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(logoutDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400WhenWeLogoutWithInvalidInput() throws Exception {
        LogoutDTO logoutDTO = new LogoutDTO();
        logoutDTO.setUsername("testname");
        logoutDTO.setSessionId("testJwt");
        when(sessionService.closeAllSessionsByUsername(logoutDTO.getUsername())).thenReturn(null);
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(logoutDTO)))
                .andExpect(status().isBadRequest()).andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        log.info("actualResponse -- {}", actualResponse);
        Assertions.assertEquals("{\"message\":\"Sessions not found.\"}", actualResponse);
    }

    @Test
    void shouldReturn400WhenWeLogoutWithoutUsername() throws Exception {
        LogoutDTO logoutDTO = new LogoutDTO();
        logoutDTO.setSessionId("testJwt");
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(logoutDTO)))
                .andExpect(status().isBadRequest()).andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(actualResponse.contains("Username cannot be empty."));
    }

    @Test
    void shouldReturn400WhenWeLogoutWithoutSessionId() throws Exception {
        LogoutDTO logoutDTO = new LogoutDTO();
        logoutDTO.setUsername("testname");
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(logoutDTO)))
                .andExpect(status().isBadRequest()).andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(actualResponse.contains("Session cannot be empty."));
    }

    @Test
    void shouldReturn400WhenWeLogout() throws Exception {
        mockMvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn415WhenWeLogoutWithXmlContentType() throws Exception {
        mockMvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturn404WhenInvalidURL() throws Exception {
        mockMvc.perform(post("/api/auth/logout/123")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn405WhenInvalidMethod() throws Exception {
        mockMvc.perform(get("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isMethodNotAllowed());
    }
}
