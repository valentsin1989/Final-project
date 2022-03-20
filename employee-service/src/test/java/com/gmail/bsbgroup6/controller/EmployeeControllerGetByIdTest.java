package com.gmail.bsbgroup6.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.bsbgroup6.errors.AuthEntryPointJwt;
import com.gmail.bsbgroup6.repository.AuthServiceRepository;
import com.gmail.bsbgroup6.repository.LegalServiceRepository;
import com.gmail.bsbgroup6.security.util.JwtUtils;
import com.gmail.bsbgroup6.service.EmployeeService;
import com.gmail.bsbgroup6.service.model.EmployeeDTO;
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

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EmployeeController.class)
class EmployeeControllerGetByIdTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthEntryPointJwt unauthorizedHandler;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private EmployeeService employeeService;
    @MockBean
    private AuthServiceRepository authServiceRepository;
    @MockBean
    private LegalServiceRepository legalServiceRepository;

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenWeGetEmployee() throws Exception {
        String token = "testToken";
        EmployeeDTO employeeDTO = new EmployeeDTO(
                1L,
                "Test Employee Name",
                "20/03/2020",
                "20/03/2022",
                "Test Legal Name",
                "BY11UNBS00000000000000000000",
                "BY12UNBS00000000000000000000"
        );

        when(employeeService.getById(1L, token)).thenReturn(employeeDTO);
        mockMvc.perform(get("/api/employees/{EmployeeId}", 1L)
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn401WhenEmployeeIdIsNotFound() throws Exception {
        String token = "testToken";
        when(employeeService.getById(1L, token)).thenReturn(null);
        mockMvc.perform(get("/api/employees/{EmployeeId}", 1L)
                        .header("Authorization", token))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldMapToBusinessModelWhenEmployeeIsInDatabase() throws Exception {
        String token = "testToken";
        EmployeeDTO employeeDTO = new EmployeeDTO(
                1L,
                "Test Employee Name",
                "20/03/2020",
                "20/03/2022",
                "Test Legal Name",
                "BY11UNBS00000000000000000000",
                "BY12UNBS00000000000000000000"
        );

        when(employeeService.getById(1L, token)).thenReturn(employeeDTO);

        MvcResult mvcResult = mockMvc.perform(get("/api/employees/{EmployeeId}", 1L)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andReturn();

        verify(employeeService, times(1)).getById(1L, token);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        EmployeeDTO result = objectMapper.readValue(actualResponseBody, EmployeeDTO.class);
        Assertions.assertEquals(employeeDTO, result);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturnErrorMessageWhenThereIsNoEmployeeInDatabase() throws Exception {
        String token = "testToken";
        when(employeeService.getById(1L, token)).thenReturn(null);

        MvcResult mvcResult = mockMvc.perform(get("/api/employees/{EmployeeId}", 1L)
                        .header("Authorization", token))
                .andExpect(status().isUnauthorized())
                .andReturn();

        verify(employeeService, times(1)).getById(1L, token);
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expectedResult = "Сотрудника не существует";
        Assertions.assertEquals(expectedResult, actualResponseBody);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWeGetEmployeeWithUnsupportedURL() throws Exception {
        mockMvc.perform(get("/api/employees/employee"))
                .andExpect(status().isBadRequest());
    }
}