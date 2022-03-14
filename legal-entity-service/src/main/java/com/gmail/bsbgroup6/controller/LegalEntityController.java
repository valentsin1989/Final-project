package com.gmail.bsbgroup6.controller;

import com.gmail.bsbgroup6.service.LegalEntityService;
import com.gmail.bsbgroup6.service.model.AddLegalEntityDTO;
import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/legals")
@AllArgsConstructor
public class LegalEntityController {

    private final LegalEntityService legalEntityService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Object> addLegalEntity(@Validated @RequestBody AddLegalEntityDTO legalEntityDTO) {
        LegalEntityDTO viewLegalEntityDTO = legalEntityService.add(legalEntityDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(viewLegalEntityDTO);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<LegalEntityDTO>> getAllLegalEntities() {
        List<LegalEntityDTO> legalEntities = legalEntityService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(legalEntities);
    }

    @GetMapping(value = "/{LegalId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Object> getLegalEntity(@PathVariable Integer LegalId) {
        Long id = Long.valueOf(LegalId);
        LegalEntityDTO legalEntity = legalEntityService.getById(id);
        if (legalEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Компания не существует"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(legalEntity);
    }
}
