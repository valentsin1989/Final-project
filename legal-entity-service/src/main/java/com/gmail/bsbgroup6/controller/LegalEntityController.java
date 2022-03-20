package com.gmail.bsbgroup6.controller;

import com.gmail.bsbgroup6.service.LegalEntityService;
import com.gmail.bsbgroup6.service.model.AddLegalEntityDTO;
import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import com.gmail.bsbgroup6.service.model.PaginationEnum;
import com.gmail.bsbgroup6.service.model.PaginationLegalEntityDTO;
import com.gmail.bsbgroup6.service.model.SearchLegalEntityDTO;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<?> getLegalEntities(
            @RequestParam(name = "pagination", required = false) PaginationEnum pagination,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "customized_page", required = false) Integer customizedPage,
            @RequestParam(name = "Name_Legal", required = false) String name,
            @RequestParam(name = "UNP", required = false) Integer unp,
            @RequestParam(name = "IBANbyBYN", required = false) String ibanByByn
    ) {
        if (pagination != null) {
            PaginationLegalEntityDTO legalEntityDTO = new PaginationLegalEntityDTO();
            legalEntityDTO.setPagination(pagination);
            legalEntityDTO.setPage(page);
            legalEntityDTO.setCustomizedPage(customizedPage);
            List<LegalEntityDTO> legalEntities = legalEntityService.getByPagination(legalEntityDTO);
            if (legalEntities.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Компании не найдены");
            }
            return ResponseEntity.status(HttpStatus.OK).body(legalEntities);
        }

        SearchLegalEntityDTO legalEntityDTO = new SearchLegalEntityDTO();
        legalEntityDTO.setName(name);
        legalEntityDTO.setUnp(unp);
        legalEntityDTO.setIbanByByn(ibanByByn);
        List<LegalEntityDTO> legalEntities = legalEntityService.getByParameters(legalEntityDTO);
        if (legalEntities.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Компания не найдена, измените параметры поиска");
        }
        return ResponseEntity.status(HttpStatus.OK).body(legalEntities);
    }

    @GetMapping(value = "/{LegalId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Object> getLegalEntity(@PathVariable Long LegalId) {
        LegalEntityDTO legalEntity = legalEntityService.getById(LegalId);
        if (legalEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Компания не существует");
        }
        return ResponseEntity.status(HttpStatus.OK).body(legalEntity);
    }
}
