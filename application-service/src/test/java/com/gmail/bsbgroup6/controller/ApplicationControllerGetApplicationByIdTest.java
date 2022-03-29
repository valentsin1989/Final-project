package com.gmail.bsbgroup6.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.bsbgroup6.controller.validator.LegalUpdateApplicationDTOValidator;
import com.gmail.bsbgroup6.controller.validator.StatusUpdateApplicationDTOValidator;
import com.gmail.bsbgroup6.errors.AuthEntryPointJwt;
import com.gmail.bsbgroup6.repository.RedisRepository;
import com.gmail.bsbgroup6.security.util.JwtUtils;
import com.gmail.bsbgroup6.service.ApplicationService;
import com.gmail.bsbgroup6.service.model.ApplicationDTO;
import com.gmail.bsbgroup6.service.model.StatusEnum;
import com.gmail.bsbgroup6.service.model.ValueEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ApplicationController.class)
class ApplicationControllerGetApplicationByIdTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthEntryPointJwt unauthorizedHandler;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private ApplicationService applicationService;
    @MockBean
    private RedisRepository redisRepository;
    @MockBean
    private StatusUpdateApplicationDTOValidator statusUpdateApplicationDTOValidator;
    @MockBean
    private LegalUpdateApplicationDTOValidator legalUpdateApplicationDTOValidator;

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenWeGetApplication() throws Exception {
        String token = "testToken";
        String uuid = "79f67ce7-abb7-24ec-b202-022400000000";
        UUID uniqueNumber = UUID.fromString(uuid);
        ApplicationDTO applicationDTO = ApplicationDTO.builder()
                .uniqueNumber(uuid)
                .status(StatusEnum.NEW.getValue())
                .employeeId(1L)
                .employeeFullName("surname")
                .conversionPercentage((float) 0.99)
                .valueLegal(ValueEnum.BYN.name())
                .valueIndividual(ValueEnum.USD.name())
                .legalEntityName("Test Legal Name")
                .build();

        when(applicationService.getByUniqueNumber(uniqueNumber, token)).thenReturn(applicationDTO);

        mockMvc.perform(get("/api/applications/{ApplicationConvId}", uuid)
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn401WhenApplicationIsNotFound() throws Exception {
        String token = "testToken";
        String uuid = "79f67ce7-abb7-24ec-b202-022400000000";
        UUID uniqueNumber = UUID.fromString(uuid);
        when(applicationService.getByUniqueNumber(uniqueNumber, token)).thenReturn(null);
        mockMvc.perform(get("/api/applications/{ApplicationConvId}", uuid)
                        .header("Authorization", token))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturnErrorMessageWhenThereIsNoApplicationInDatabase() throws Exception {
        String token = "testToken";
        String uuid = "79f67ce7-abb7-24ec-b202-022400000000";
        UUID uniqueNumber = UUID.fromString(uuid);

        when(applicationService.getByUniqueNumber(uniqueNumber, token)).thenReturn(null);

        MvcResult mvcResult = mockMvc.perform(get("/api/applications/{ApplicationConvId}", uuid)
                        .header("Authorization", token))
                .andExpect(status().isUnauthorized())
                .andReturn();

        verify(applicationService, times(1)).getByUniqueNumber(uniqueNumber, token);
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expectedResult = "Заявление на конверсию от сотрудника не существует";
        Assertions.assertEquals(expectedResult, actualResponseBody);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldMapToBusinessModelWhenApplicationIsInDatabase() throws Exception {
        String token = "testToken";
        String uuid = "79f67ce7-abb7-24ec-b202-022400000000";
        UUID uniqueNumber = UUID.fromString(uuid);
        ApplicationDTO applicationDTO = ApplicationDTO.builder()
                .uniqueNumber(uuid)
                .status(StatusEnum.NEW.getValue())
                .employeeId(1L)
                .employeeFullName("surname")
                .conversionPercentage((float) 0.99)
                .valueLegal(ValueEnum.BYN.name())
                .valueIndividual(ValueEnum.USD.name())
                .legalEntityName("Test Legal Name")
                .build();

        when(applicationService.getByUniqueNumber(uniqueNumber, token)).thenReturn(applicationDTO);

        MvcResult mvcResult = mockMvc.perform(get("/api/applications/{ApplicationConvId}", uuid)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andReturn();

        verify(applicationService, times(1)).getByUniqueNumber(uniqueNumber, token);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        ApplicationDTO result = objectMapper.readValue(actualResponseBody, ApplicationDTO.class);
        Assertions.assertEquals(applicationDTO, result);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWeGetApplicationWithUnsupportedURL() throws Exception {
        mockMvc.perform(get("/api/applications/app"))
                .andExpect(status().isBadRequest());
    }
}
