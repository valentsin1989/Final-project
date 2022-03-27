package com.gmail.bsbgroup6.controller.validator;

import com.gmail.bsbgroup6.repository.ApplicationRepository;
import com.gmail.bsbgroup6.repository.LegalServiceRepository;
import com.gmail.bsbgroup6.repository.model.Application;
import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import com.gmail.bsbgroup6.service.model.LegalUpdateApplicationDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class LegalUpdateApplicationDTOValidator {

    private final ApplicationRepository applicationRepository;
    private final LegalServiceRepository legalServiceRepository;

    public boolean isLinkedToLegalEntity(LegalUpdateApplicationDTO applicationDTO, String token) {
        String uniqueNumber = applicationDTO.getApplicationConvId();
        UUID uuid = UUID.fromString(uniqueNumber);
        Application application = applicationRepository.findByUUID(uuid).orElse(null);
        Long legalEntityId = null;
        if (application != null) {
            legalEntityId = application.getLegalEntityId();
        }
        LegalEntityDTO legalEntity = legalServiceRepository.getLegalById(legalEntityId, token);
        String name = legalEntity.getName();
        String legalName = applicationDTO.getLegalEntityName();
        return legalName.equals(name);
    }
}
