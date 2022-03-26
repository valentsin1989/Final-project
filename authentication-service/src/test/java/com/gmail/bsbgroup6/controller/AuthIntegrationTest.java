package com.gmail.bsbgroup6.controller;

import com.gmail.bsbgroup6.service.model.AddUserDTO;
import com.gmail.bsbgroup6.service.model.AddedUserDTO;
import com.gmail.bsbgroup6.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthIntegrationTest extends BaseIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    void contextLoads() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        AddUserDTO addUserDTO = new AddUserDTO();
        addUserDTO.setUsername("testname");
        addUserDTO.setPassword("testPass123");
        addUserDTO.setUsermail("allen@example.com");
        addUserDTO.setFirstName("ТестовоеИмя");

        HttpEntity<Object> entity = new HttpEntity<>(addUserDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/auth/signin"),
                HttpMethod.POST,
                entity,
                String.class
        );
        assertTrue(response.getStatusCode().equals(HttpStatus.CREATED));
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}