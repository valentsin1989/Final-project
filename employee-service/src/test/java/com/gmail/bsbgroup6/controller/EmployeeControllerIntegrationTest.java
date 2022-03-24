package com.gmail.bsbgroup6.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gmail.bsbgroup6.security.util.JwtUtils;
import com.gmail.bsbgroup6.service.model.AddEmployeeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.Assert.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "eureka.client.enabled:false"
)
@ActiveProfiles("test")
class EmployeeControllerIntegrationTest extends BaseIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    @WithMockUser(roles = {"USER"})
    void contextLoads() throws JsonProcessingException {
        String token = jwtUtils.generateJwtToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(
                "Test Employee Name",
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(1),
                "TestLegalName",
                "BY11UNBS00000000000000000000",
                "BY12UNBS00000000000000000000"
        );

        HttpEntity<AddEmployeeDTO> entity = new HttpEntity<>(addEmployeeDTO, headers);
        ResponseEntity<AddEmployeeDTO> response = restTemplate.exchange(
                createURLWithPort("/api/employees"),
                HttpMethod.POST,
                entity,
                AddEmployeeDTO.class
        );
        assertTrue(response.getStatusCode().equals(HttpStatus.CREATED));
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}