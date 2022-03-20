package com.gmail.bsbgroup6.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.bsbgroup6.errors.AuthEntryPointJwt;
import com.gmail.bsbgroup6.repository.AuthServiceRepository;
import com.gmail.bsbgroup6.repository.LegalServiceRepository;
import com.gmail.bsbgroup6.security.util.JwtUtils;
import com.gmail.bsbgroup6.service.EmployeeService;
import com.gmail.bsbgroup6.service.model.EmployeeDTO;
import com.gmail.bsbgroup6.service.model.GetEmployeeDTO;
import com.gmail.bsbgroup6.service.model.PaginationEmployeeDTO;
import com.gmail.bsbgroup6.service.model.PaginationEnum;
import com.gmail.bsbgroup6.service.model.SearchEmployeeDTO;
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
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EmployeeController.class)
class EmployeeControllerGetEmployeesTest {

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
    void shouldReturn200WhenWeGetEmployeesByDefaultPagination() throws Exception {
        String token = "testToken";
        PaginationEmployeeDTO paginationEmployeeDTO = new PaginationEmployeeDTO();
        paginationEmployeeDTO.setPagination(PaginationEnum.DEFAULT);
        paginationEmployeeDTO.setPage(1);
        paginationEmployeeDTO.setCustomizedPage(20);
        List<EmployeeDTO> employees = IntStream.rangeClosed(1, 10)
                .mapToObj(this::getEmployee)
                .collect(Collectors.toList());

        when(employeeService.getByPagination(paginationEmployeeDTO, token)).thenReturn(employees);

        mockMvc.perform(get("/api/employees")
                        .param("pagination", "DEFAULT")
                        .param("page", String.valueOf(1))
                        .param("customized_page", String.valueOf(20))
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenWeGetEmployeesByCustomizedPagination() throws Exception {
        String token = "testToken";
        PaginationEmployeeDTO paginationEmployeeDTO = new PaginationEmployeeDTO();
        paginationEmployeeDTO.setPagination(PaginationEnum.CUSTOMED);
        paginationEmployeeDTO.setPage(1);
        paginationEmployeeDTO.setCustomizedPage(20);
        List<EmployeeDTO> employees = IntStream.rangeClosed(1, 20)
                .mapToObj(this::getEmployee)
                .collect(Collectors.toList());

        when(employeeService.getByPagination(paginationEmployeeDTO, token)).thenReturn(employees);

        mockMvc.perform(get("/api/employees")
                        .param("pagination", "CUSTOMED")
                        .param("page", String.valueOf(1))
                        .param("customized_page", String.valueOf(20))
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldMapToBusinessModelWhenEmployeesAreInDatabase() throws Exception {
        String token = "testToken";
        PaginationEmployeeDTO paginationEmployeeDTO = new PaginationEmployeeDTO();
        paginationEmployeeDTO.setPagination(PaginationEnum.CUSTOMED);
        paginationEmployeeDTO.setPage(1);
        paginationEmployeeDTO.setCustomizedPage(20);
        List<EmployeeDTO> employees = IntStream.rangeClosed(1, 20)
                .mapToObj(this::getEmployee)
                .collect(Collectors.toList());

        when(employeeService.getByPagination(paginationEmployeeDTO, token)).thenReturn(employees);

        MvcResult mvcResult = mockMvc.perform(get("/api/employees")
                        .param("pagination", "CUSTOMED")
                        .param("page", String.valueOf(1))
                        .param("customized_page", String.valueOf(20))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andReturn();

        verify(employeeService, times(1)).getByPagination(paginationEmployeeDTO, token);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        List<EmployeeDTO> result = objectMapper.readValue(actualResponseBody,
                objectMapper.getTypeFactory().constructCollectionType(List.class, EmployeeDTO.class));
        Assertions.assertEquals(employees, result);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn401WhenEmployeesAreNotFoundByPagination() throws Exception {
        String token = "testToken";
        PaginationEmployeeDTO paginationEmployeeDTO = new PaginationEmployeeDTO();
        paginationEmployeeDTO.setPagination(PaginationEnum.CUSTOMED);
        paginationEmployeeDTO.setPage(1);
        paginationEmployeeDTO.setCustomizedPage(20);
        List<EmployeeDTO> employees = Collections.emptyList();

        when(employeeService.getByPagination(paginationEmployeeDTO, token)).thenReturn(employees);

        mockMvc.perform(get("/api/employees")
                        .param("pagination", "CUSTOMED")
                        .param("page", String.valueOf(1))
                        .param("customized_page", String.valueOf(20))
                        .header("Authorization", token))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturnErrorMessageWhenEmployeesAreNotFoundByPagination() throws Exception {
        String token = "testToken";
        PaginationEmployeeDTO paginationEmployeeDTO = new PaginationEmployeeDTO();
        paginationEmployeeDTO.setPagination(PaginationEnum.CUSTOMED);
        paginationEmployeeDTO.setPage(1);
        paginationEmployeeDTO.setCustomizedPage(20);
        List<EmployeeDTO> employees = Collections.emptyList();

        when(employeeService.getByPagination(paginationEmployeeDTO, token)).thenReturn(employees);

        MvcResult mvcResult = mockMvc.perform(get("/api/employees")
                        .param("pagination", "CUSTOMED")
                        .param("page", String.valueOf(1))
                        .param("customized_page", String.valueOf(20))
                        .header("Authorization", token))
                .andExpect(status().isUnauthorized())
                .andReturn();

        verify(employeeService, times(1)).getByPagination(paginationEmployeeDTO, token);
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expectedResult = "Сотрудники не найдены";
        Assertions.assertEquals(expectedResult, actualResponseBody);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenWeGetEmployeeByLegalEntityNameAndUnpAndEmployeeFullName() throws Exception {
        String token = "TestToken";
        SearchEmployeeDTO searchEmployeeDTO = new SearchEmployeeDTO();
        searchEmployeeDTO.setLegalEntityName("Name");
        searchEmployeeDTO.setUnp(100);
        searchEmployeeDTO.setFullName("Test Name");
        List<GetEmployeeDTO> employees = IntStream.rangeClosed(1, 5)
                .mapToObj(this::getEmployeeDTO)
                .collect(Collectors.toList());

        when(employeeService.getByParameters(searchEmployeeDTO, token)).thenReturn(employees);

        mockMvc.perform(get("/api/employees")
                        .param("Name_Legal", "Name")
                        .param("UNP", String.valueOf(100))
                        .param("Full_Name_Individual", "Test Name")
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenWeGetEmployeeByLegalEntityNameAndEmployeeFullName() throws Exception {
        String token = "TestToken";
        SearchEmployeeDTO searchEmployeeDTO = new SearchEmployeeDTO();
        searchEmployeeDTO.setLegalEntityName("Name");
        searchEmployeeDTO.setFullName("Test Name");
        List<GetEmployeeDTO> employees = IntStream.rangeClosed(1, 5)
                .mapToObj(this::getEmployeeDTO)
                .collect(Collectors.toList());

        when(employeeService.getByParameters(searchEmployeeDTO, token)).thenReturn(employees);

        mockMvc.perform(get("/api/employees")
                        .param("Name_Legal", "Name")
                        .param("Full_Name_Individual", "Test Name")
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenWeGetEmployeeByUnpAndEmployeeFullName() throws Exception {
        String token = "TestToken";
        SearchEmployeeDTO searchEmployeeDTO = new SearchEmployeeDTO();
        searchEmployeeDTO.setUnp(100);
        searchEmployeeDTO.setFullName("Test Name");
        List<GetEmployeeDTO> employees = IntStream.rangeClosed(1, 5)
                .mapToObj(this::getEmployeeDTO)
                .collect(Collectors.toList());

        when(employeeService.getByParameters(searchEmployeeDTO, token)).thenReturn(employees);

        mockMvc.perform(get("/api/employees")
                        .param("UNP", String.valueOf(100))
                        .param("Full_Name_Individual", "Test Name")
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldMapToBusinessModelWhenSearchedEmployeesAreInDatabase() throws Exception {
        String token = "TestToken";
        SearchEmployeeDTO searchEmployeeDTO = new SearchEmployeeDTO();
        searchEmployeeDTO.setLegalEntityName("Name");
        searchEmployeeDTO.setUnp(100);
        searchEmployeeDTO.setFullName("Test Name");
        List<GetEmployeeDTO> employees = IntStream.rangeClosed(1, 5)
                .mapToObj(this::getEmployeeDTO)
                .collect(Collectors.toList());

        when(employeeService.getByParameters(searchEmployeeDTO, token)).thenReturn(employees);

        MvcResult mvcResult = mockMvc.perform(get("/api/employees")
                        .param("Name_Legal", "Name")
                        .param("UNP", String.valueOf(100))
                        .param("Full_Name_Individual", "Test Name")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andReturn();

        verify(employeeService, times(1)).getByParameters(searchEmployeeDTO, token);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        List<GetEmployeeDTO> result = objectMapper.readValue(actualResponseBody,
                objectMapper.getTypeFactory().constructCollectionType(List.class, GetEmployeeDTO.class));
        Assertions.assertEquals(employees, result);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn401WhenSearchedEmployeeIsNotFound() throws Exception {
        String token = "TestToken";
        SearchEmployeeDTO searchEmployeeDTO = new SearchEmployeeDTO();
        searchEmployeeDTO.setLegalEntityName("Name");
        searchEmployeeDTO.setUnp(100);
        searchEmployeeDTO.setFullName("Test Name");
        List<GetEmployeeDTO> employees = Collections.emptyList();

        when(employeeService.getByParameters(searchEmployeeDTO, token)).thenReturn(employees);

        mockMvc.perform(get("/api/employees")
                        .param("Name_Legal", "Name")
                        .param("UNP", String.valueOf(100))
                        .param("Full_Name_Individual", "Test Name")
                        .header("Authorization", token))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturnErrorMessageWhenSearchedEmployeeIsNotFound() throws Exception {
        String token = "TestToken";
        SearchEmployeeDTO searchEmployeeDTO = new SearchEmployeeDTO();
        searchEmployeeDTO.setLegalEntityName("Name");
        searchEmployeeDTO.setUnp(100);
        searchEmployeeDTO.setFullName("Test Name");
        List<GetEmployeeDTO> employees = Collections.emptyList();

        when(employeeService.getByParameters(searchEmployeeDTO, token)).thenReturn(employees);

        MvcResult mvcResult = mockMvc.perform(get("/api/employees")
                        .param("Name_Legal", "Name")
                        .param("UNP", String.valueOf(100))
                        .param("Full_Name_Individual", "Test Name")
                        .header("Authorization", token))
                .andExpect(status().isUnauthorized())
                .andReturn();

        verify(employeeService, times(1)).getByParameters(searchEmployeeDTO, token);
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expectedResult = "Сотрудник не найден, измените параметры поиска";
        Assertions.assertEquals(expectedResult, actualResponseBody);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn404WhenWeGetEmployeesWithUnsupportedURL() throws Exception {
        mockMvc.perform(get("/api/employeess"))
                .andExpect(status().isNotFound());
    }

    private EmployeeDTO getEmployee(int count) {
        return new EmployeeDTO(
                1L + count,
                "Test Employee Name" + count,
                "20/03/2020",
                "20/03/2022",
                "Test Legal Name",
                "BY11UNBS0000000000000000000" + count,
                "BY12UNBS0000000000000000000" + count
        );
    }

    private GetEmployeeDTO getEmployeeDTO(int count) {
        return new GetEmployeeDTO(
                1L + count,
                "20/03/2020",
                "20/03/2022",
                "Test Legal Name",
                "BY11UNBS0000000000000000000" + count,
                "BY12UNBS0000000000000000000" + count
        );
    }
}