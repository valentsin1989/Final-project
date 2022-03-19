package com.gmail.bsbgroup6.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.bsbgroup6.controller.validator.LegalEntityValidator;
import com.gmail.bsbgroup6.errors.AuthEntryPointJwt;
import com.gmail.bsbgroup6.repository.EmployeeServiceRepository;
import com.gmail.bsbgroup6.security.util.JwtUtils;
import com.gmail.bsbgroup6.service.LegalEntityService;
import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import com.gmail.bsbgroup6.service.model.PaginationEnum;
import com.gmail.bsbgroup6.service.model.PaginationLegalEntityDTO;
import com.gmail.bsbgroup6.service.model.SearchLegalEntityDTO;
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
@WebMvcTest(controllers = LegalEntityController.class)
class LegalEntityControllerGetLegalEntitiesTest {

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
    private EmployeeServiceRepository employeeServiceRepository;

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenWeGetLegalEntitiesByDefaultPagination() throws Exception {
        PaginationLegalEntityDTO paginationLegalEntityDTO = new PaginationLegalEntityDTO();
        paginationLegalEntityDTO.setPagination(PaginationEnum.DEFAULT);
        paginationLegalEntityDTO.setPage(1);
        paginationLegalEntityDTO.setCustomizedPage(20);
        List<LegalEntityDTO> legalEntities = IntStream.rangeClosed(1, 10)
                .mapToObj(this::getLegalEntity)
                .collect(Collectors.toList());

        when(legalEntityService.getByPagination(paginationLegalEntityDTO)).thenReturn(legalEntities);

        mockMvc.perform(get("/api/legals")
                        .param("pagination", "DEFAULT")
                        .param("page", String.valueOf(1))
                        .param("customized_page", String.valueOf(20)))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenWeGetLegalEntitiesByCustomizedPagination() throws Exception {
        PaginationLegalEntityDTO paginationLegalEntityDTO = new PaginationLegalEntityDTO();
        paginationLegalEntityDTO.setPagination(PaginationEnum.CUSTOMED);
        paginationLegalEntityDTO.setPage(1);
        paginationLegalEntityDTO.setCustomizedPage(20);
        List<LegalEntityDTO> legalEntities = IntStream.rangeClosed(1, 20)
                .mapToObj(this::getLegalEntity)
                .collect(Collectors.toList());

        when(legalEntityService.getByPagination(paginationLegalEntityDTO)).thenReturn(legalEntities);

        mockMvc.perform(get("/api/legals")
                        .param("pagination", "CUSTOMED")
                        .param("page", String.valueOf(1))
                        .param("customized_page", String.valueOf(20)))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldMapToBusinessModelWhenLegalEntitiesAreInDatabase() throws Exception {
        PaginationLegalEntityDTO paginationLegalEntityDTO = new PaginationLegalEntityDTO();
        paginationLegalEntityDTO.setPagination(PaginationEnum.DEFAULT);
        paginationLegalEntityDTO.setPage(1);
        paginationLegalEntityDTO.setCustomizedPage(20);
        List<LegalEntityDTO> legalEntities = IntStream.rangeClosed(1, 10)
                .mapToObj(this::getLegalEntity)
                .collect(Collectors.toList());

        when(legalEntityService.getByPagination(paginationLegalEntityDTO)).thenReturn(legalEntities);

        MvcResult mvcResult = mockMvc.perform(get("/api/legals")
                        .param("pagination", "DEFAULT")
                        .param("page", String.valueOf(1))
                        .param("customized_page", String.valueOf(20)))
                .andExpect(status().isOk())
                .andReturn();

        verify(legalEntityService, times(1)).getByPagination(paginationLegalEntityDTO);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        List<LegalEntityDTO> result = objectMapper.readValue(actualResponseBody,
                objectMapper.getTypeFactory().constructCollectionType(List.class, LegalEntityDTO.class));
        Assertions.assertEquals(legalEntities, result);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenLegalEntitiesAreNotFoundByPagination() throws Exception {
        PaginationLegalEntityDTO paginationLegalEntityDTO = new PaginationLegalEntityDTO();
        paginationLegalEntityDTO.setPagination(PaginationEnum.CUSTOMED);
        paginationLegalEntityDTO.setPage(1);
        paginationLegalEntityDTO.setCustomizedPage(20);
        List<LegalEntityDTO> legalEntities = Collections.emptyList();

        when(legalEntityService.getByPagination(paginationLegalEntityDTO)).thenReturn(legalEntities);

        mockMvc.perform(get("/api/legals")
                        .param("pagination", "CUSTOMED")
                        .param("page", String.valueOf(1))
                        .param("customized_page", String.valueOf(20)))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturnErrorMessageWhenLegalEntitiesAreNotFoundByPagination() throws Exception {
        PaginationLegalEntityDTO paginationLegalEntityDTO = new PaginationLegalEntityDTO();
        paginationLegalEntityDTO.setPagination(PaginationEnum.CUSTOMED);
        paginationLegalEntityDTO.setPage(1);
        paginationLegalEntityDTO.setCustomizedPage(20);
        List<LegalEntityDTO> legalEntities = Collections.emptyList();

        when(legalEntityService.getByPagination(paginationLegalEntityDTO)).thenReturn(legalEntities);

        MvcResult mvcResult = mockMvc.perform(get("/api/legals")
                        .param("pagination", "CUSTOMED")
                        .param("page", String.valueOf(1))
                        .param("customized_page", String.valueOf(20)))
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(legalEntityService, times(1)).getByPagination(paginationLegalEntityDTO);
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expectedResult = "Компании не найдены";
        Assertions.assertEquals(expectedResult, actualResponseBody);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenWeGetLegalEntitiesByNameAndUnpAndIban() throws Exception {
        SearchLegalEntityDTO searchLegalEntityDTO = new SearchLegalEntityDTO();
        searchLegalEntityDTO.setName("Name");
        searchLegalEntityDTO.setUnp(100);
        searchLegalEntityDTO.setIbanByByn("BY00");
        List<LegalEntityDTO> legalEntities = IntStream.rangeClosed(1, 10)
                .mapToObj(this::getLegalEntity)
                .collect(Collectors.toList());

        when(legalEntityService.getByParameters(searchLegalEntityDTO)).thenReturn(legalEntities);

        mockMvc.perform(get("/api/legals")
                        .param("Name_Legal", "Name")
                        .param("UNP", String.valueOf(100))
                        .param("IBANbyBYN", "BY00"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenWeGetLegalEntitiesByName() throws Exception {
        SearchLegalEntityDTO searchLegalEntityDTO = new SearchLegalEntityDTO();
        searchLegalEntityDTO.setName("Name");
        List<LegalEntityDTO> legalEntities = IntStream.rangeClosed(1, 10)
                .mapToObj(this::getLegalEntity)
                .collect(Collectors.toList());

        when(legalEntityService.getByParameters(searchLegalEntityDTO)).thenReturn(legalEntities);

        mockMvc.perform(get("/api/legals")
                        .param("Name_Legal", "Name"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenWeGetLegalEntitiesByUnp() throws Exception {
        SearchLegalEntityDTO searchLegalEntityDTO = new SearchLegalEntityDTO();
        searchLegalEntityDTO.setUnp(100);
        List<LegalEntityDTO> legalEntities = IntStream.rangeClosed(1, 10)
                .mapToObj(this::getLegalEntity)
                .collect(Collectors.toList());

        when(legalEntityService.getByParameters(searchLegalEntityDTO)).thenReturn(legalEntities);

        mockMvc.perform(get("/api/legals")
                        .param("UNP", String.valueOf(100)))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenWeGetLegalEntitiesByIban() throws Exception {
        SearchLegalEntityDTO searchLegalEntityDTO = new SearchLegalEntityDTO();
        searchLegalEntityDTO.setIbanByByn("BY00");
        List<LegalEntityDTO> legalEntities = IntStream.rangeClosed(1, 10)
                .mapToObj(this::getLegalEntity)
                .collect(Collectors.toList());

        when(legalEntityService.getByParameters(searchLegalEntityDTO)).thenReturn(legalEntities);

        mockMvc.perform(get("/api/legals")
                        .param("IBANbyBYN", "BY00"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenWeGetLegalEntitiesByNameAndUnp() throws Exception {
        SearchLegalEntityDTO searchLegalEntityDTO = new SearchLegalEntityDTO();
        searchLegalEntityDTO.setName("Name");
        searchLegalEntityDTO.setUnp(100);
        List<LegalEntityDTO> legalEntities = IntStream.rangeClosed(1, 10)
                .mapToObj(this::getLegalEntity)
                .collect(Collectors.toList());

        when(legalEntityService.getByParameters(searchLegalEntityDTO)).thenReturn(legalEntities);

        mockMvc.perform(get("/api/legals")
                        .param("Name_Legal", "Name")
                        .param("UNP", String.valueOf(100)))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenWeGetLegalEntitiesByNameAndIban() throws Exception {
        SearchLegalEntityDTO searchLegalEntityDTO = new SearchLegalEntityDTO();
        searchLegalEntityDTO.setName("Name");
        searchLegalEntityDTO.setIbanByByn("BY00");
        List<LegalEntityDTO> legalEntities = IntStream.rangeClosed(1, 10)
                .mapToObj(this::getLegalEntity)
                .collect(Collectors.toList());

        when(legalEntityService.getByParameters(searchLegalEntityDTO)).thenReturn(legalEntities);

        mockMvc.perform(get("/api/legals")
                        .param("Name_Legal", "Name")
                        .param("IBANbyBYN", "BY00"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenWeGetLegalEntitiesByUnpAndIban() throws Exception {
        SearchLegalEntityDTO searchLegalEntityDTO = new SearchLegalEntityDTO();
        searchLegalEntityDTO.setUnp(100);
        searchLegalEntityDTO.setIbanByByn("BY00");
        List<LegalEntityDTO> legalEntities = IntStream.rangeClosed(1, 10)
                .mapToObj(this::getLegalEntity)
                .collect(Collectors.toList());

        when(legalEntityService.getByParameters(searchLegalEntityDTO)).thenReturn(legalEntities);

        mockMvc.perform(get("/api/legals")
                        .param("UNP", String.valueOf(100))
                        .param("IBANbyBYN", "BY00"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldMapToBusinessModelWhenSearchedByParametersLegalEntitiesAreInDatabase() throws Exception {
        SearchLegalEntityDTO searchLegalEntityDTO = new SearchLegalEntityDTO();
        searchLegalEntityDTO.setName("Name");
        searchLegalEntityDTO.setUnp(100);
        searchLegalEntityDTO.setIbanByByn("BY00");
        List<LegalEntityDTO> legalEntities = IntStream.rangeClosed(1, 10)
                .mapToObj(this::getLegalEntity)
                .collect(Collectors.toList());

        when(legalEntityService.getByParameters(searchLegalEntityDTO)).thenReturn(legalEntities);

        MvcResult mvcResult = mockMvc.perform(get("/api/legals")
                        .param("Name_Legal", "Name")
                        .param("UNP", String.valueOf(100))
                        .param("IBANbyBYN", "BY00"))
                .andExpect(status().isOk())
                .andReturn();

        verify(legalEntityService, times(1)).getByParameters(searchLegalEntityDTO);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        List<LegalEntityDTO> result = objectMapper.readValue(actualResponseBody,
                objectMapper.getTypeFactory().constructCollectionType(List.class, LegalEntityDTO.class));
        Assertions.assertEquals(legalEntities, result);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn401WhenLegalEntitiesAreNotFoundByParameters() throws Exception {
        SearchLegalEntityDTO searchLegalEntityDTO = new SearchLegalEntityDTO();
        searchLegalEntityDTO.setName("Name");
        searchLegalEntityDTO.setUnp(100);
        searchLegalEntityDTO.setIbanByByn("BY00");
        List<LegalEntityDTO> legalEntities = Collections.emptyList();

        when(legalEntityService.getByParameters(searchLegalEntityDTO)).thenReturn(legalEntities);

        mockMvc.perform(get("/api/legals")
                        .param("Name_Legal", "Name")
                        .param("UNP", String.valueOf(100))
                        .param("IBANbyBYN", "BY00"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturnErrorMessageWhenLegalEntitiesAreNotFoundByParameters() throws Exception {
        SearchLegalEntityDTO searchLegalEntityDTO = new SearchLegalEntityDTO();
        searchLegalEntityDTO.setName("Name");
        searchLegalEntityDTO.setUnp(100);
        searchLegalEntityDTO.setIbanByByn("BY00");
        List<LegalEntityDTO> legalEntities = Collections.emptyList();

        when(legalEntityService.getByParameters(searchLegalEntityDTO)).thenReturn(legalEntities);

        MvcResult mvcResult = mockMvc.perform(get("/api/legals")
                        .param("Name_Legal", "Name")
                        .param("UNP", String.valueOf(100))
                        .param("IBANbyBYN", "BY00"))
                .andExpect(status().isUnauthorized())
                .andReturn();

        verify(legalEntityService, times(1)).getByParameters(searchLegalEntityDTO);
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expectedResult = "Компания не найдена, измените параметры поиска";
        Assertions.assertEquals(expectedResult, actualResponseBody);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn404WhenWeGetLegalEntitiesWithUnsupportedURL() throws Exception {
        mockMvc.perform(get("/api/legalss"))
                .andExpect(status().isNotFound());
    }

    private LegalEntityDTO getLegalEntity(int count) {
        LegalEntityDTO legalEntityDTO = new LegalEntityDTO();
        legalEntityDTO.setId(1L + count);
        legalEntityDTO.setName("Name" + count);
        legalEntityDTO.setUnp(100000000 + count);
        legalEntityDTO.setIbanByByn("BY00UNBS0000000000000000000" + count);
        legalEntityDTO.setType("RESIDENT");
        legalEntityDTO.setTotalEmployees(100);
        return legalEntityDTO;
    }
}