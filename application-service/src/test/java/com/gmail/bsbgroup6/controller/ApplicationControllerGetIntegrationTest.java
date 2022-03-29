package com.gmail.bsbgroup6.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gmail.bsbgroup6.repository.RedisRepository;
import com.gmail.bsbgroup6.security.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "eureka.client.enabled:false"
)
@ActiveProfiles("test")
class ApplicationControllerGetIntegrationTest extends BaseIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtUtils jwtUtils;

    @MockBean
    private RedisRepository redisRepository;

    @Test
    @WithMockUser(roles = {"USER"})
    void contextLoads() throws JsonProcessingException {
        String token = jwtUtils.generateJwtToken();
        String UUID = "5d23c39c-af77-11ec-b909-0242ac120002";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        when(redisRepository.isExist(token)).thenReturn(true);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/applications/{ApplicationConvId}"),
                HttpMethod.GET,
                entity,
                String.class,
                UUID
        );
        assertTrue(response.getStatusCode().equals(HttpStatus.UNAUTHORIZED));
        assertEquals("Заявление на конверсию от сотрудника не существует", response.getBody());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}