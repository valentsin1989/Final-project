package com.gmail.bsbgroup6.controller;

import com.gmail.bsbgroup6.controller.validator.LegalUpdateApplicationDTOValidator;
import com.gmail.bsbgroup6.service.ApplicationService;
import com.gmail.bsbgroup6.service.model.AddApplicationDTO;
import com.gmail.bsbgroup6.service.model.AddedApplicationDTO;
import com.gmail.bsbgroup6.service.model.ApplicationDTO;
import com.gmail.bsbgroup6.service.model.LegalUpdateApplicationDTO;
import com.gmail.bsbgroup6.service.model.PaginationApplicationDTO;
import com.gmail.bsbgroup6.service.model.PaginationEnum;
import com.gmail.bsbgroup6.service.model.StatusUpdateApplicationDTO;
import com.gmail.bsbgroup6.service.model.UpdatedByStatusApplicationDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final LegalUpdateApplicationDTOValidator legalUpdateValidator;

    @PostMapping(value = "/api/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Object> addApplication(
            @RequestParam(name = "dtoFile") MultipartFile dtoFile,
            @RequestHeader(value = "Authorization") String token
    ) {
        List<AddApplicationDTO> applications = applicationService.getFromFile(dtoFile);

        List<AddedApplicationDTO> existingApplications = new ArrayList<>();
        for (AddApplicationDTO application : applications) {
            AddedApplicationDTO addedApplicationDTO = applicationService.getByUUID(application.getUniqueNumber());
            if (addedApplicationDTO != null) {
                existingApplications.add(addedApplicationDTO);
            }
        }
        if (!existingApplications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Файл содержит дубль заявки " + existingApplications);
        }

        List<AddedApplicationDTO> addedApplications = applications.stream()
                .map(applicationDTO -> applicationService.add(applicationDTO, token))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body("Заявки добавлены: " + addedApplications);
    }

    @GetMapping(value = "/api/applications")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> getEmployees(
            @RequestParam(name = "pagination") PaginationEnum pagination,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "customized_page") Integer customizedPage,
            @RequestHeader(value = "Authorization") String token
    ) {
        PaginationApplicationDTO applicationDTO = new PaginationApplicationDTO();
        applicationDTO.setPagination(pagination);
        applicationDTO.setPage(page);
        applicationDTO.setCustomizedPage(customizedPage);
        List<ApplicationDTO> applications = applicationService.getByPagination(applicationDTO, token);
        if (applications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Заявки не найдены");
        }
        return ResponseEntity.status(HttpStatus.OK).body(applications);
    }

    @GetMapping(value = "/api/applications/{ApplicationConvId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Object> getApplication(
            @PathVariable String ApplicationConvId,
            @RequestHeader(value = "Authorization") String token
    ) {
        UUID uuid = UUID.fromString(ApplicationConvId);
        ApplicationDTO applicationDTO = applicationService.getByUniqueNumber(uuid, token);
        if (applicationDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Заявление на конверсию от сотрудника не существует");
        }
        return ResponseEntity.status(HttpStatus.OK).body(applicationDTO);
    }

    @PutMapping(value = "/api/applications")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Object> updateApplicationByStatus(
            @RequestBody @Validated StatusUpdateApplicationDTO applicationDTO,
            @RequestHeader(value = "Authorization") String token
    ) {
        UpdatedByStatusApplicationDTO updatedApplicationDTO = applicationService.updateStatus(applicationDTO, token);
        return ResponseEntity.status(HttpStatus.OK).body(updatedApplicationDTO);
    }

    @PutMapping(value = "/api/applications/{ApplicationConvId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Object> updateApplicationByNameLegal(
            @PathVariable String ApplicationConvId,
            @RequestParam(name = "Name_Legal") String legalEntityName,
            @RequestHeader(value = "Authorization") String token
    ) {
        LegalUpdateApplicationDTO applicationDTO = new LegalUpdateApplicationDTO();
        applicationDTO.setApplicationConvId(ApplicationConvId);
        applicationDTO.setLegalEntityName(legalEntityName);
        boolean isLinkedToLegalEntity = legalUpdateValidator.isLinkedToLegalEntity(applicationDTO, token);
        if (isLinkedToLegalEntity) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(String.format(
                            "Заявка на конверсию %s привязана к компании %s", ApplicationConvId, legalEntityName
                    ));
        }
        LegalUpdateApplicationDTO updatedApplicationDTO = applicationService.updateLegal(applicationDTO, token);
        if (updatedApplicationDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format(
                            "Компания %s не существует", legalEntityName
                    ));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(String.format(
                        "Заявка на конверсию %s перепривязана к %s", ApplicationConvId, legalEntityName
                ));
    }
}