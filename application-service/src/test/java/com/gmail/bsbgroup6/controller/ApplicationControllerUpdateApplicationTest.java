package com.gmail.bsbgroup6.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.bsbgroup6.controller.validator.LegalUpdateApplicationDTOValidator;
import com.gmail.bsbgroup6.controller.validator.StatusUpdateApplicationDTOValidator;
import com.gmail.bsbgroup6.errors.AuthEntryPointJwt;
import com.gmail.bsbgroup6.repository.RedisRepository;
import com.gmail.bsbgroup6.security.util.JwtUtils;
import com.gmail.bsbgroup6.service.ApplicationService;
import com.gmail.bsbgroup6.service.model.LegalUpdateApplicationDTO;
import com.gmail.bsbgroup6.service.model.StatusEnum;
import com.gmail.bsbgroup6.service.model.StatusUpdateApplicationDTO;
import com.gmail.bsbgroup6.service.model.UpdatedByStatusApplicationDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ApplicationController.class)
class ApplicationControllerUpdateApplicationTest {

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
    void shouldReturn200WhenWeUpdateApplicationWithValidStatus() throws Exception {
        String token = "testToken";
        String uuid = "79f67ce7-abb7-24ec-b202-022400000000";
        StatusUpdateApplicationDTO updateApplicationDTO = new StatusUpdateApplicationDTO();
        updateApplicationDTO.setApplicationConvId(uuid);
        updateApplicationDTO.setStatus(StatusEnum.IN_PROGRESS.getValue());

        when(statusUpdateApplicationDTOValidator.isValidStatus(updateApplicationDTO)).thenReturn(true);

        mockMvc.perform(put("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateApplicationDTO))
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenWeUpdateApplication() throws Exception {
        String token = "testToken";
        String uuid = "79f67ce7-abb7-24ec-b202-022400000000";

        StatusUpdateApplicationDTO updateApplicationDTO = new StatusUpdateApplicationDTO();
        updateApplicationDTO.setApplicationConvId(uuid);
        updateApplicationDTO.setStatus(StatusEnum.IN_PROGRESS.getValue());
        when(statusUpdateApplicationDTOValidator.isValidStatus(updateApplicationDTO)).thenReturn(true);

        UpdatedByStatusApplicationDTO updatedApplication = new UpdatedByStatusApplicationDTO();
        updatedApplication.setStatus(StatusEnum.IN_PROGRESS.getValue());
        updatedApplication.setUser("user");
        when(applicationService.updateStatus(updateApplicationDTO, token)).thenReturn(updatedApplication);

        mockMvc.perform(put("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateApplicationDTO))
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn415WhenWePutApplicationWithXmlContentType() throws Exception {
        String token = "testToken";
        mockMvc.perform(put("/api/applications")
                        .contentType(MediaType.APPLICATION_XML_VALUE)
                        .header("Authorization", token))
                .andExpect(status().isUnsupportedMediaType());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePutApplicationWithUnsupportedURL() throws Exception {
        mockMvc.perform(get("/api/applications/app"))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWeUpdateApplicationWithInvalidStatus() throws Exception {
        String token = "testToken";
        String uuid = "79f67ce7-abb7-24ec-b202-022400000000";
        StatusUpdateApplicationDTO updateApplicationDTO = new StatusUpdateApplicationDTO();
        updateApplicationDTO.setApplicationConvId(uuid);
        updateApplicationDTO.setStatus(StatusEnum.IN_PROGRESS.getValue());

        when(statusUpdateApplicationDTOValidator.isValidStatus(updateApplicationDTO)).thenReturn(false);

        mockMvc.perform(put("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateApplicationDTO))
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturnErrorMessageWhenWeUpdateApplicationWithInvalidStatus() throws Exception {
        String token = "testToken";
        String uuid = "79f67ce7-abb7-24ec-b202-022400000000";
        StatusUpdateApplicationDTO updateApplicationDTO = new StatusUpdateApplicationDTO();
        updateApplicationDTO.setApplicationConvId(uuid);
        updateApplicationDTO.setStatus(StatusEnum.IN_PROGRESS.getValue());

        when(statusUpdateApplicationDTOValidator.isValidStatus(updateApplicationDTO)).thenReturn(false);

        MvcResult mvcResult = mockMvc.perform(put("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateApplicationDTO))
                        .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Статус не может быть изменен", errors.get(0));
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePutApplicationWithEmptyBody() throws Exception {
        mockMvc.perform(put("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "testToken"))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldMapToBusinessModelWhenApplicationIsSuccessfullyUpdated() throws Exception {
        String token = "testToken";
        String uuid = "79f67ce7-abb7-24ec-b202-022400000000";

        StatusUpdateApplicationDTO updateApplicationDTO = new StatusUpdateApplicationDTO();
        updateApplicationDTO.setApplicationConvId(uuid);
        updateApplicationDTO.setStatus(StatusEnum.IN_PROGRESS.getValue());
        when(statusUpdateApplicationDTOValidator.isValidStatus(updateApplicationDTO)).thenReturn(true);

        UpdatedByStatusApplicationDTO updatedApplication = new UpdatedByStatusApplicationDTO();
        updatedApplication.setStatus(StatusEnum.IN_PROGRESS.getValue());
        updatedApplication.setUser("user");
        when(applicationService.updateStatus(updateApplicationDTO, token)).thenReturn(updatedApplication);

        MvcResult mvcResult = mockMvc.perform(put("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateApplicationDTO))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andReturn();

        verify(applicationService, times(1)).updateStatus(updateApplicationDTO, token);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        UpdatedByStatusApplicationDTO result = objectMapper.readValue(
                actualResponseBody, UpdatedByStatusApplicationDTO.class
        );
        Assertions.assertEquals(updatedApplication, result);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenWeTryToUpdateApplicationAlreadyLinkedToLegalEntity() throws Exception {
        String token = "testToken";
        String uuid = "79f67ce7-abb7-24ec-b202-022400000000";
        String legalEntityName = "Test Name";
        LegalUpdateApplicationDTO updateApplicationDTO = new LegalUpdateApplicationDTO();
        updateApplicationDTO.setApplicationConvId(uuid);
        updateApplicationDTO.setLegalEntityName(legalEntityName);

        when(legalUpdateApplicationDTOValidator.isLinkedToLegalEntity(updateApplicationDTO, token))
                .thenReturn(true);

        mockMvc.perform(put("/api/applications/{ApplicationConvId}", uuid)
                        .param("Name_Legal", legalEntityName)
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturnMessageWhenWeTryToUpdateApplicationAlreadyLinkedToLegalEntity() throws Exception {
        String token = "testToken";
        String uuid = "79f67ce7-abb7-24ec-b202-022400000000";
        String legalEntityName = "Test Name";
        LegalUpdateApplicationDTO updateApplicationDTO = new LegalUpdateApplicationDTO();
        updateApplicationDTO.setApplicationConvId(uuid);
        updateApplicationDTO.setLegalEntityName(legalEntityName);

        when(legalUpdateApplicationDTOValidator.isLinkedToLegalEntity(updateApplicationDTO, token))
                .thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(put("/api/applications/{ApplicationConvId}", uuid)
                        .param("Name_Legal", legalEntityName)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andReturn();

        verify(legalUpdateApplicationDTOValidator, times(1))
                .isLinkedToLegalEntity(updateApplicationDTO, token);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResult = String.format(
                "Заявка на конверсию %s привязана к компании %s", uuid, legalEntityName
        );
        Assertions.assertEquals(expectedResult, actualResponseBody);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenLegalEntityDoesNotExist() throws Exception {
        String token = "testToken";
        String uuid = "79f67ce7-abb7-24ec-b202-022400000000";
        String legalEntityName = "Test Name";
        LegalUpdateApplicationDTO updateApplicationDTO = new LegalUpdateApplicationDTO();
        updateApplicationDTO.setApplicationConvId(uuid);
        updateApplicationDTO.setLegalEntityName(legalEntityName);

        when(legalUpdateApplicationDTOValidator.isLinkedToLegalEntity(updateApplicationDTO, token))
                .thenReturn(false);
        when(applicationService.updateLegal(updateApplicationDTO, token)).thenReturn(null);

        mockMvc.perform(put("/api/applications/{ApplicationConvId}", uuid)
                        .param("Name_Legal", legalEntityName)
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePutLegalEntityWithEmptyUniqueNumber() throws Exception {
        String token = "testToken";
        String legalEntityName = "Test Name";

        mockMvc.perform(put("/api/applications/{ApplicationConvId}", (Object) null)
                        .param("Name_Legal", legalEntityName)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn400WhenWePutLegalEntityWithMissingLegalNameParameter() throws Exception {
        String token = "testToken";
        String uuid = "79f67ce7-abb7-24ec-b202-022400000000";

        mockMvc.perform(put("/api/applications/{ApplicationConvId}", uuid)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturnErrorMessageWhenLegalEntityDoesNotExist() throws Exception {
        String token = "testToken";
        String uuid = "79f67ce7-abb7-24ec-b202-022400000000";
        String legalEntityName = "Test Name";
        LegalUpdateApplicationDTO updateApplicationDTO = new LegalUpdateApplicationDTO();
        updateApplicationDTO.setApplicationConvId(uuid);
        updateApplicationDTO.setLegalEntityName(legalEntityName);

        when(legalUpdateApplicationDTOValidator.isLinkedToLegalEntity(updateApplicationDTO, token))
                .thenReturn(false);
        when(applicationService.updateLegal(updateApplicationDTO, token)).thenReturn(null);

        MvcResult mvcResult = mockMvc.perform(put("/api/applications/{ApplicationConvId}", uuid)
                        .param("Name_Legal", legalEntityName)
                        .header("Authorization", token))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(applicationService, times(1)).updateLegal(updateApplicationDTO, token);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResult = String.format("Компания %s не существует", legalEntityName);
        Assertions.assertEquals(expectedResult, actualResponseBody);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldReturn200WhenApplicationIsSuccessfullyLinkedToLegalEntity() throws Exception {
        String token = "testToken";
        String uuid = "79f67ce7-abb7-24ec-b202-022400000000";
        String legalEntityName = "Test Name";
        LegalUpdateApplicationDTO updateApplicationDTO = new LegalUpdateApplicationDTO();
        updateApplicationDTO.setApplicationConvId(uuid);
        updateApplicationDTO.setLegalEntityName(legalEntityName);

        when(legalUpdateApplicationDTOValidator.isLinkedToLegalEntity(updateApplicationDTO, token))
                .thenReturn(false);
        when(applicationService.updateLegal(updateApplicationDTO, token)).thenReturn(updateApplicationDTO);

        mockMvc.perform(put("/api/applications/{ApplicationConvId}", uuid)
                        .param("Name_Legal", legalEntityName)
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldMapToBusinessModelWhenApplicationIsSuccessfullyLinkedToLegalEntity() throws Exception {
        String token = "testToken";
        String uuid = "79f67ce7-abb7-24ec-b202-022400000000";
        String legalEntityName = "Test Name";
        LegalUpdateApplicationDTO updateApplicationDTO = new LegalUpdateApplicationDTO();
        updateApplicationDTO.setApplicationConvId(uuid);
        updateApplicationDTO.setLegalEntityName(legalEntityName);

        when(legalUpdateApplicationDTOValidator.isLinkedToLegalEntity(updateApplicationDTO, token))
                .thenReturn(false);
        when(applicationService.updateLegal(updateApplicationDTO, token)).thenReturn(updateApplicationDTO);

        MvcResult mvcResult = mockMvc.perform(put("/api/applications/{ApplicationConvId}", uuid)
                        .param("Name_Legal", legalEntityName)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andReturn();

        verify(applicationService, times(1)).updateLegal(updateApplicationDTO, token);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResult = String.format("Заявка на конверсию %s перепривязана к %s", uuid, legalEntityName);
        Assertions.assertEquals(expectedResult, actualResponseBody);
    }
}