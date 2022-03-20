package com.gmail.bsbgroup6.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.bsbgroup6.controller.validator.UserValidator;
import com.gmail.bsbgroup6.service.SessionService;
import com.gmail.bsbgroup6.service.UserService;
import com.gmail.bsbgroup6.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
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
class AuthControllerSessionTest {
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
    void shouldReturn200WhenWeGetStatusSession() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        String jwtToken = "jwtToken";
        httpHeaders.setBearerAuth(jwtToken);
        when(jwtUtils.parseJwtFromHeaders(httpHeaders)).thenReturn(jwtToken);
        when(jwtUtils.validateJwtToken(jwtToken)).thenReturn(true);
        when(sessionService.isActiveSession(jwtToken)).thenReturn(true);
        mockMvc.perform(post("/api/auth/session")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(httpHeaders)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn200WhenWeGetStatusSessionWithoutHttpHeaders() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/session")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("DISABLE", actualResponse);
    }

    @Test
    void shouldReturn415WhenWeGetStatusSessionWithXmlContentType() throws Exception {
        mockMvc.perform(post("/api/auth/session")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturn404WhenInvalidURL() throws Exception {
        mockMvc.perform(post("/api/auth/session/123")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn405WhenInvalidMethod() throws Exception {
        mockMvc.perform(get("/api/auth/session")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isMethodNotAllowed());
    }
}

