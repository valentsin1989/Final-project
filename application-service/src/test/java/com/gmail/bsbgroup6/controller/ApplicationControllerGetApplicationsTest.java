package com.gmail.bsbgroup6.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.bsbgroup6.controller.validator.LegalUpdateApplicationDTOValidator;
import com.gmail.bsbgroup6.controller.validator.StatusUpdateApplicationDTOValidator;
import com.gmail.bsbgroup6.errors.AuthEntryPointJwt;
import com.gmail.bsbgroup6.repository.RedisRepository;
import com.gmail.bsbgroup6.security.util.JwtUtils;
import com.gmail.bsbgroup6.service.ApplicationService;
import com.gmail.bsbgroup6.service.model.ApplicationDTO;
import com.gmail.bsbgroup6.service.model.PaginationApplicationDTO;
import com.gmail.bsbgroup6.service.model.PaginationEnum;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ApplicationController.class)
class ApplicationControllerGetApplicationsTest {

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
    void shouldReturn200WhenWeGetApplicationsByDefaultPagination() throws Exception {
        String token = "testToken";
        PaginationApplicationDTO paginationApplicationDTO = new PaginationApplicationDTO();
        paginationApplicationDTO.setPagination(PaginationEnum.DEFAULT);
        paginationApplicationDTO.setPage(1);
        paginationApplicationDTO.setCustomizedPage(20);
        List<ApplicationDTO> applications = IntStream.rangeClosed(1, 10)
                .mapToObj(this::getApplication)
                .collect(Collectors.toList());

        when(applicationService.getByPagination(paginationApplicationDTO, token)).thenReturn(applications);

        mockMvc.perform(get("/api/applications")
                        .param("pagination", "DEFAULT")
                        .param("page", String.valueOf(1))
                        .param("customized_page", String.valueOf(20))
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenWeGetApplicationsByCustomizedPagination() throws Exception {
        String token = "testToken";
        PaginationApplicationDTO paginationApplicationDTO = new PaginationApplicationDTO();
        paginationApplicationDTO.setPagination(PaginationEnum.CUSTOMED);
        paginationApplicationDTO.setPage(1);
        paginationApplicationDTO.setCustomizedPage(20);
        List<ApplicationDTO> applications = IntStream.rangeClosed(1, 20)
                .mapToObj(this::getApplication)
                .collect(Collectors.toList());

        when(applicationService.getByPagination(paginationApplicationDTO, token)).thenReturn(applications);

        mockMvc.perform(get("/api/applications")
                        .param("pagination", "CUSTOMED")
                        .param("page", String.valueOf(1))
                        .param("customized_page", String.valueOf(20))
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldMapToBusinessModelWhenApplicationsAreInDatabase() throws Exception {
        String token = "testToken";
        PaginationApplicationDTO paginationApplicationDTO = new PaginationApplicationDTO();
        paginationApplicationDTO.setPagination(PaginationEnum.DEFAULT);
        paginationApplicationDTO.setPage(1);
        paginationApplicationDTO.setCustomizedPage(20);
        List<ApplicationDTO> applications = IntStream.rangeClosed(1, 10)
                .mapToObj(this::getApplication)
                .collect(Collectors.toList());

        when(applicationService.getByPagination(paginationApplicationDTO, token)).thenReturn(applications);

        MvcResult mvcResult = mockMvc.perform(get("/api/applications")
                        .param("pagination", "DEFAULT")
                        .param("page", String.valueOf(1))
                        .param("customized_page", String.valueOf(20))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andReturn();

        verify(applicationService, times(1)).getByPagination(paginationApplicationDTO, token);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        List<ApplicationDTO> result = objectMapper.readValue(actualResponseBody,
                objectMapper.getTypeFactory().constructCollectionType(List.class, ApplicationDTO.class));
        Assertions.assertEquals(applications, result);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn401WhenApplicationsAreNotFoundByPagination() throws Exception {
        String token = "testToken";
        PaginationApplicationDTO paginationApplicationDTO = new PaginationApplicationDTO();
        paginationApplicationDTO.setPagination(PaginationEnum.DEFAULT);
        paginationApplicationDTO.setPage(1);
        paginationApplicationDTO.setCustomizedPage(20);
        List<ApplicationDTO> applications = Collections.emptyList();

        when(applicationService.getByPagination(paginationApplicationDTO, token)).thenReturn(applications);

        mockMvc.perform(get("/api/applications")
                        .param("pagination", "DEFAULT")
                        .param("page", String.valueOf(1))
                        .param("customized_page", String.valueOf(20))
                        .header("Authorization", token))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturnErrorMessageWhenApplicationsAreNotFoundByPagination() throws Exception {
        String token = "testToken";
        PaginationApplicationDTO paginationApplicationDTO = new PaginationApplicationDTO();
        paginationApplicationDTO.setPagination(PaginationEnum.DEFAULT);
        paginationApplicationDTO.setPage(1);
        paginationApplicationDTO.setCustomizedPage(20);
        List<ApplicationDTO> applications = Collections.emptyList();

        when(applicationService.getByPagination(paginationApplicationDTO, token)).thenReturn(applications);

        MvcResult mvcResult = mockMvc.perform(get("/api/applications")
                        .param("pagination", "DEFAULT")
                        .param("page", String.valueOf(1))
                        .param("customized_page", String.valueOf(20))
                        .header("Authorization", token))
                .andExpect(status().isUnauthorized())
                .andReturn();

        verify(applicationService, times(1)).getByPagination(paginationApplicationDTO, token);
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expectedResult = "Заявки не найдены";
        Assertions.assertEquals(expectedResult, actualResponseBody);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn404WhenWeGetApplicationsWithUnsupportedURL() throws Exception {
        mockMvc.perform(get("/api/applicationss"))
                .andExpect(status().isNotFound());
    }

    private ApplicationDTO getApplication(int count) {
        return ApplicationDTO.builder()
                .uniqueNumber("79f67ce7-abb7-24ec-b202-02240000000" + count)
                .status(StatusEnum.NEW.getValue())
                .employeeId(1L + count)
                .employeeFullName("surname" + count)
                .conversionPercentage((float) 0.99)
                .valueLegal(ValueEnum.BYN.name())
                .valueIndividual(ValueEnum.USD.name())
                .legalEntityName("Test Legal Name")
                .build();
    }
}