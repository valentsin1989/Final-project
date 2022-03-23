package com.gmail.bsbgroup6.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.gmail.bsbgroup6.security.util.JwtUtils;
import com.gmail.bsbgroup6.service.model.AddEmployeeDTO;
import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class EmployeeControllerIntegrationTest extends BaseIT {

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtUtils jwtUtils;

    private WireMockServer authService;

    private WireMockServer legalService;

    @BeforeEach
    void setup() {
        authService = new WireMockServer(wireMockConfig().port(8090));
        authService.start();
        legalService = new WireMockServer(wireMockConfig().port(8080));
        legalService.start();
    }

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
        List<LegalEntityDTO> legals = new ArrayList<>();
        LegalEntityDTO legalEntityDTO = new LegalEntityDTO();
        legalEntityDTO.setId(1L);
        legalEntityDTO.setName("Test Name");
        legalEntityDTO.setUnp(123456789);
        legalEntityDTO.setIbanByByn("BY00UNBS00000000000000000000");
        legalEntityDTO.setType("RESIDENT");
        legalEntityDTO.setTotalEmployees(100);
        legals.add(legalEntityDTO);

        authService.stubFor(post(urlEqualTo("/api/auth/session"))
                .willReturn(aResponse().withHeader("Content-Type", "text/plain")
                        .withBody("ENABLE")));
        legalService.stubFor(get(urlEqualTo("/api/legals?Name_Legal=TestLegalName"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(legals))));

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