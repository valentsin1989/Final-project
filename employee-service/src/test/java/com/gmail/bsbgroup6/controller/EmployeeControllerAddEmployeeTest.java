package com.gmail.bsbgroup6.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.bsbgroup6.controller.validator.EmployeeValidator;
import com.gmail.bsbgroup6.errors.AuthEntryPointJwt;
import com.gmail.bsbgroup6.repository.LegalServiceRepository;
import com.gmail.bsbgroup6.repository.RedisRepository;
import com.gmail.bsbgroup6.security.util.JwtUtils;
import com.gmail.bsbgroup6.service.EmployeeService;
import com.gmail.bsbgroup6.service.model.AddEmployeeDTO;
import com.gmail.bsbgroup6.service.model.AddedEmployeeDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EmployeeController.class)
class EmployeeControllerAddEmployeeTest {

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
    private RedisRepository redisRepository;
    @MockBean
    private LegalServiceRepository legalServiceRepository;
    @MockBean
    private EmployeeValidator employeeValidator;

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn201WhenWePostEmployeeWithValidInput() throws Exception {
        String token = "testToken";
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(
                "Test Employee Name",
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(1),
                "Test Legal Name",
                "BY11UNBS00000000000000000000",
                "BY12UNBS00000000000000000000"
        );
        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        addedEmployeeDTO.setId(1L);

        when(employeeService.add(addEmployeeDTO, token)).thenReturn(addedEmployeeDTO);

        mockMvc.perform(post("/api/employees")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addEmployeeDTO)))
                .andExpect(status().isCreated());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn415WhenWePostEmployeeWithXmlContentType() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isUnsupportedMediaType());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn405WhenWePostEmployeeWithUnsupportedURL() throws Exception {
        mockMvc.perform(post("/api/employees/employee")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isMethodNotAllowed());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePostEmployeeWithEmptyRequestBody() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenNotUniqueEmployeeInput() throws Exception {
        String token = "testToken";
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(
                "Test Employee Name",
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(1),
                "Test Legal Name",
                "BY11UNBS00000000000000000000",
                "BY12UNBS00000000000000000000"
        );

        when(employeeValidator.isEmployeeExists(addEmployeeDTO)).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(post("/api/employees")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addEmployeeDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Сотрудник существует", errors.get(0));
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePostEmployeeWithEmptyFullName() throws Exception {
        String token = "testToken";
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(
                " ",
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(1),
                "Test Legal Name",
                "BY11UNBS00000000000000000000",
                "BY12UNBS00000000000000000000"
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/employees")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addEmployeeDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Поле Full_Name_Individual не может быть пустым", errors.get(0));
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePostEmployeeWithNullFullName() throws Exception {
        String token = "testToken";
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(
                null,
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(1),
                "Test Legal Name",
                "BY11UNBS00000000000000000000",
                "BY12UNBS00000000000000000000"
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/employees")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addEmployeeDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Поле Full_Name_Individual не может быть пустым", errors.get(0));
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePostEmployeeWithFullNameLongerThanMaxLength() throws Exception {
        String token = "testToken";
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(
                "Test Employee Name Test Employee Name Test Employee Name Test Employee Name " +
                        "Test Employee Name Test Employee Name Test Employee Name Test Employee Name " +
                        "Test Employee Name Test Employee Name Test Employee Name Test Employee Name " +
                        "Test Employee Name Test Employee Name",
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(1),
                "Test Legal Name",
                "BY11UNBS00000000000000000000",
                "BY12UNBS00000000000000000000"
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/employees")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addEmployeeDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Неверно заданы параметры", errors.get(0));
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePostEmployeeWithRecruitmentDateInFuture() throws Exception {
        String token = "testToken";
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(
                "Test Employee Name",
                LocalDate.now().plusMonths(1),
                LocalDate.now().plusMonths(1),
                "Test Legal Name",
                "BY11UNBS00000000000000000000",
                "BY12UNBS00000000000000000000"
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/employees")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addEmployeeDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Неверно заданы параметры", errors.get(0));
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePostEmployeeWithTerminationDateInPast() throws Exception {
        String token = "testToken";
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(
                "Test Employee Name",
                LocalDate.now().minusMonths(1),
                LocalDate.now().minusMonths(1),
                "Test Legal Name",
                "BY11UNBS00000000000000000000",
                "BY12UNBS00000000000000000000"
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/employees")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addEmployeeDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Неверно заданы параметры", errors.get(0));
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePostEmployeeWithEmptyPersonalIbanByn() throws Exception {
        String token = "testToken";
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(
                "Test Employee Name",
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(1),
                "Test Legal Name",
                " ",
                "BY12UNBS00000000000000000000"
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/employees")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addEmployeeDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Неверно заданы параметры", errors.get(0));
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePostEmployeeWithNullPersonalIbanByn() throws Exception {
        String token = "testToken";
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(
                "Test Employee Name",
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(1),
                "Test Legal Name",
                null,
                "BY12UNBS00000000000000000000"
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/employees")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addEmployeeDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Неверно заданы параметры", errors.get(0));
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePostEmployeeWithPersonalIbanBynShorterThanRequiredLength() throws Exception {
        String token = "testToken";
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(
                "Test Employee Name",
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(1),
                "Test Legal Name",
                "BY11UNBS0000000000000000000",
                "BY12UNBS00000000000000000000"
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/employees")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addEmployeeDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Неверно заданы параметры", errors.get(0));
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePostEmployeeWithPersonalIbanBynLongerThanRequiredLength() throws Exception {
        String token = "testToken";
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(
                "Test Employee Name",
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(1),
                "Test Legal Name",
                "BY11UNBS000000000000000000000",
                "BY12UNBS00000000000000000000"
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/employees")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addEmployeeDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Неверно заданы параметры", errors.get(0));
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePostEmployeeWithPersonalIbanBynOfInvalidPattern() throws Exception {
        String token = "testToken";
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(
                "Test Employee Name",
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(1),
                "Test Legal Name",
                "RU11UNBS00000000000000000000",
                "BY12UNBS00000000000000000000"
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/employees")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addEmployeeDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Неверно заданы параметры", errors.get(0));
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePostEmployeeWithEmptyPersonalIbanCurrency() throws Exception {
        String token = "testToken";
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(
                "Test Employee Name",
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(1),
                "Test Legal Name",
                "BY12UNBS00000000000000000000",
                " "
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/employees")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addEmployeeDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Неверно заданы параметры", errors.get(0));
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePostEmployeeWithNullPersonalIbanCurrency() throws Exception {
        String token = "testToken";
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(
                "Test Employee Name",
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(1),
                "Test Legal Name",
                "BY12UNBS00000000000000000000",
                null
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/employees")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addEmployeeDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Неверно заданы параметры", errors.get(0));
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePostEmployeeWithPersonalIbanCurrencyShorterThanRequiredLength() throws Exception {
        String token = "testToken";
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(
                "Test Employee Name",
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(1),
                "Test Legal Name",
                "BY11UNBS00000000000000000000",
                "BY12UNBS0000000000000000000"
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/employees")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addEmployeeDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Неверно заданы параметры", errors.get(0));
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePostEmployeeWithPersonalIbanCurrencyLongerThanRequiredLength() throws Exception {
        String token = "testToken";
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(
                "Test Employee Name",
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(1),
                "Test Legal Name",
                "BY11UNBS00000000000000000000",
                "BY12UNBS000000000000000000000"
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/employees")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addEmployeeDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Неверно заданы параметры", errors.get(0));
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldMapToBusinessModelInPostRequestWhenWeAddEmployee() throws Exception {
        String token = "testToken";
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(
                "Test Employee Name",
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(1),
                "Test Legal Name",
                "BY11UNBS00000000000000000000",
                "BY12UNBS00000000000000000000"
        );
        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        addedEmployeeDTO.setId(1L);

        when(employeeService.add(addEmployeeDTO, token)).thenReturn(addedEmployeeDTO);

        MvcResult mvcResult = mockMvc.perform(post("/api/employees")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addEmployeeDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(employeeService, times(1)).add(addEmployeeDTO, token);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        AddedEmployeeDTO result = objectMapper.readValue(actualResponseBody, AddedEmployeeDTO.class);
        Assertions.assertEquals(addedEmployeeDTO, result);
    }
}