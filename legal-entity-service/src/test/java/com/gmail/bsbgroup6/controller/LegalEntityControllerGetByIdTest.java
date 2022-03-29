package com.gmail.bsbgroup6.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.bsbgroup6.controller.validator.LegalEntityValidator;
import com.gmail.bsbgroup6.errors.AuthEntryPointJwt;
import com.gmail.bsbgroup6.repository.RedisRepository;
import com.gmail.bsbgroup6.security.util.JwtUtils;
import com.gmail.bsbgroup6.service.LegalEntityService;
import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = LegalEntityController.class)
class LegalEntityControllerGetByIdTest {

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
    RedisRepository redisRepository;

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenWeGetLegalEntity() throws Exception {
        LegalEntityDTO legalEntityDTO = new LegalEntityDTO();
        legalEntityDTO.setId(1L);
        legalEntityDTO.setName("Test Name");
        legalEntityDTO.setUnp(123456789);
        legalEntityDTO.setIbanByByn("BY00UNBS00000000000000000000");
        legalEntityDTO.setType("RESIDENT");
        legalEntityDTO.setTotalEmployees(100);

        when(legalEntityService.getById(1L)).thenReturn(legalEntityDTO);

        mockMvc.perform(get("/api/legals/{LegalId}", 1L))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn404WhenLegalIdIsNotFound() throws Exception {
        when(legalEntityService.getById(1L)).thenReturn(null);
        mockMvc.perform(get("/api/legals/{LegalId}", 1L))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldMapToBusinessModelWhenLegalEntityIsInDatabase() throws Exception {
        LegalEntityDTO legalEntityDTO = new LegalEntityDTO();
        legalEntityDTO.setId(1L);
        legalEntityDTO.setName("Test Name");
        legalEntityDTO.setUnp(123456789);
        legalEntityDTO.setIbanByByn("BY00UNBS00000000000000000000");
        legalEntityDTO.setType("RESIDENT");
        legalEntityDTO.setTotalEmployees(100);

        when(legalEntityService.getById(1L)).thenReturn(legalEntityDTO);

        MvcResult mvcResult = mockMvc.perform(get("/api/legals/{LegalId}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        verify(legalEntityService, times(1)).getById(1L);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        LegalEntityDTO result = objectMapper.readValue(actualResponseBody, LegalEntityDTO.class);
        Assertions.assertEquals(legalEntityDTO, result);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturnErrorMessageWhenThereIsNoLegalEntityInDatabase() throws Exception {
        when(legalEntityService.getById(1L)).thenReturn(null);

        MvcResult mvcResult = mockMvc.perform(get("/api/legals/{LegalId}", 1L))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(legalEntityService, times(1)).getById(1L);
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expectedResult = "Компания не существует";
        Assertions.assertEquals(expectedResult, actualResponseBody);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWeGetLegalEntityWithUnsupportedURL() throws Exception {
        mockMvc.perform(get("/api/legals/legal"))
                .andExpect(status().isBadRequest());
    }
}