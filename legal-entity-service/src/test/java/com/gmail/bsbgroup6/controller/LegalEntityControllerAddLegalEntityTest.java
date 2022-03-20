package com.gmail.bsbgroup6.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.bsbgroup6.controller.validator.LegalEntityValidator;
import com.gmail.bsbgroup6.errors.AuthEntryPointJwt;
import com.gmail.bsbgroup6.repository.AuthenticationServiceRepository;
import com.gmail.bsbgroup6.security.util.JwtUtils;
import com.gmail.bsbgroup6.service.LegalEntityService;
import com.gmail.bsbgroup6.service.model.AddLegalEntityDTO;
import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import com.gmail.bsbgroup6.service.model.LegalTypeEnum;
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
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = LegalEntityController.class)
class LegalEntityControllerAddLegalEntityTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthEntryPointJwt unauthorizedHandler;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private LegalEntityService legalEntityService;
    @MockBean
    private LegalEntityValidator legalEntityValidator;
    @MockBean
    private AuthenticationServiceRepository employeeServiceRepository;

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenWePostEntityWithValidInput() throws Exception {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO(
                "Test Name",
                123456789,
                "BY00UNBS00000000000000000000",
                LegalTypeEnum.RESIDENT,
                100
        );
        LegalEntityDTO legalEntityDTO = new LegalEntityDTO();
        legalEntityDTO.setId(1L);
        legalEntityDTO.setName("Test Name");
        legalEntityDTO.setUnp(123456789);
        legalEntityDTO.setIbanByByn("BY00UNBS00000000000000000000");
        legalEntityDTO.setType("RESIDENT");
        legalEntityDTO.setTotalEmployees(100);

        when(legalEntityService.add(addLegalEntityDTO)).thenReturn(legalEntityDTO);

        mockMvc.perform(post("/api/legals")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addLegalEntityDTO)))
                .andExpect(status().isCreated());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn415WhenWePostLegalEntityWithXmlContentType() throws Exception {
        mockMvc.perform(post("/api/legals")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isUnsupportedMediaType());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePostLegalEntityWithUnsupportedURL() throws Exception {
        mockMvc.perform(get("/api/legals/legal")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePostLegalEntityWithEmptyRequestBody() throws Exception {
        mockMvc.perform(post("/api/legals")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenNotUniqueLegalEntityInput() throws Exception {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO(
                "Test Name",
                123456789,
                "BY00UNBS00000000000000000000",
                LegalTypeEnum.RESIDENT,
                100
        );

        when(legalEntityValidator.isLegalEntityExists(addLegalEntityDTO)).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(post("/api/legals")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addLegalEntityDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Компания существует", errors.get(0));
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePostLegalEntityWithEmptyName() throws Exception {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO(
                " ",
                123456789,
                "BY00UNBS00000000000000000000",
                LegalTypeEnum.RESIDENT,
                100
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/legals")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addLegalEntityDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Поле Name_Legal не может быть пустым", errors.get(0));
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePostLegalEntityWithNullName() throws Exception {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO(
                null,
                123456789,
                "BY00UNBS00000000000000000000",
                LegalTypeEnum.RESIDENT,
                100
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/legals")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addLegalEntityDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Поле Name_Legal не может быть пустым", errors.get(0));
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePostLegalEntityWithNameLongerThanMaxLength() throws Exception {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO(
                "Test Name Test Name Test Name Test Name Test Name Test Name Test Name " +
                        "Test Name Test Name Test Name Test Name Test Name Test Name Test Name " +
                        "Test Name Test Name Test Name Test Name Test Name Test Name Test Name " +
                        "Test Name Test Name Test Name Test Name Test Name",
                123456789,
                "BY00UNBS00000000000000000000",
                LegalTypeEnum.RESIDENT,
                100
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/legals")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addLegalEntityDTO)))
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
    void shouldReturn400WhenWePostLegalEntityWithUnpShorterThanRequiredLength() throws Exception {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO(
                "Test Name",
                12345678,
                "BY00UNBS00000000000000000000",
                LegalTypeEnum.RESIDENT,
                100
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/legals")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addLegalEntityDTO)))
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
    void shouldReturn400WhenWePostLegalEntityWithUnpLongerThanRequiredLength() throws Exception {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO(
                "Test Name",
                1234567891,
                "BY00UNBS00000000000000000000",
                LegalTypeEnum.RESIDENT,
                100
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/legals")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addLegalEntityDTO)))
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
    void shouldReturn400WhenWePostLegalEntityWithEmptyIban() throws Exception {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO(
                "Test Name",
                123456789,
                " ",
                LegalTypeEnum.RESIDENT,
                100
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/legals")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addLegalEntityDTO)))
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
    void shouldReturn400WhenWePostLegalEntityWithNullIban() throws Exception {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO(
                "Test Name",
                123456789,
                null,
                LegalTypeEnum.RESIDENT,
                100
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/legals")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addLegalEntityDTO)))
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
    void shouldReturn400WhenWePostLegalEntityWithIbanShorterThanRequiredLength() throws Exception {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO(
                "Test Name",
                123456789,
                "BY00UNBS0000000000000000000",
                LegalTypeEnum.RESIDENT,
                100
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/legals")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addLegalEntityDTO)))
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
    void shouldReturn400WhenWePostLegalEntityWithIbanLongerThanRequiredLength() throws Exception {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO(
                "Test Name",
                123456789,
                "BY00UNBS000000000000000000000",
                LegalTypeEnum.RESIDENT,
                100
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/legals")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addLegalEntityDTO)))
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
    void shouldReturn400WhenWePostLegalEntityWithIbanOfInvalidPattern() throws Exception {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO(
                "Test Name",
                123456789,
                "RU00UNBS00000000000000000000",
                LegalTypeEnum.RESIDENT,
                100
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/legals")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addLegalEntityDTO)))
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
    void shouldReturn400WhenWePostLegalEntityWithCountOfEmployeesBiggerThanMaxValue() throws Exception {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO(
                "Test Name",
                123456789,
                "BY00UNBS00000000000000000000",
                LegalTypeEnum.RESIDENT,
                10000
        );

        MvcResult mvcResult = mockMvc.perform(post("/api/legals")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addLegalEntityDTO)))
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
    void shouldReturn201WhenWePostLegalEntityWithCountOfEmployeesEqualToMaxValue() throws Exception {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO(
                "Test Name",
                123456789,
                "BY00UNBS00000000000000000000",
                LegalTypeEnum.RESIDENT,
                1000
        );

        mockMvc.perform(post("/api/legals")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addLegalEntityDTO)))
                .andExpect(status().isCreated());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldMapToBusinessModelInPostRequestWhenWeAddLegalEntity() throws Exception {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO(
                "Test Name",
                123456789,
                "BY00UNBS00000000000000000000",
                LegalTypeEnum.RESIDENT,
                100
        );
        LegalEntityDTO legalEntityDTO = new LegalEntityDTO();
        legalEntityDTO.setId(1L);
        legalEntityDTO.setName("Test Name");
        legalEntityDTO.setUnp(123456789);
        legalEntityDTO.setIbanByByn("BY00UNBS00000000000000000000");
        legalEntityDTO.setType("RESIDENT");
        legalEntityDTO.setTotalEmployees(100);

        when(legalEntityService.add(addLegalEntityDTO)).thenReturn(legalEntityDTO);

        MvcResult mvcResult = mockMvc.perform(post("/api/legals")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addLegalEntityDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(legalEntityService, times(1)).add(addLegalEntityDTO);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        LegalEntityDTO result = objectMapper.readValue(actualResponseBody, LegalEntityDTO.class);
        Assertions.assertEquals(legalEntityDTO, result);
    }
}