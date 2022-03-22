package com.gmail.bsbgroup6.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.gmail.bsbgroup6.security.util.JwtUtils;
import com.gmail.bsbgroup6.service.model.AddLegalEntityDTO;
import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import com.gmail.bsbgroup6.service.model.LegalTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class LegalEntityControllerIntegrationTest extends BaseIT {

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtUtils jwtUtils;

    private WireMockServer authService;


    @BeforeEach
    void setup() {
        authService = new WireMockServer(wireMockConfig().port(8090));
        authService.start();
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void contextLoads() throws JsonProcessingException {
        String token = jwtUtils.generateJwtToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO(
                "Test Name",
                123456789,
                "BY00UNBS00000000000000000000",
                LegalTypeEnum.RESIDENT,
                100
        );

        authService.stubFor(post(urlEqualTo("/api/auth/session"))
                .willReturn(aResponse().withHeader("Content-Type", "text/plain")
                        .withBody("ENABLE")));

        HttpEntity<AddLegalEntityDTO> entity = new HttpEntity<>(addLegalEntityDTO, headers);
        ResponseEntity<LegalEntityDTO> response = restTemplate.exchange(
                createURLWithPort("/api/legals"),
                HttpMethod.POST,
                entity,
                LegalEntityDTO.class
        );
        assertTrue(response.getStatusCode().equals(HttpStatus.CREATED));
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}