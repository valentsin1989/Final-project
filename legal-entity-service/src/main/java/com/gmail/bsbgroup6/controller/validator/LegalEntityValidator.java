package com.gmail.bsbgroup6.controller.validator;

import com.gmail.bsbgroup6.service.LegalEntityService;
import com.gmail.bsbgroup6.service.model.AddLegalEntityDTO;
import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class LegalEntityValidator {

    private final LegalEntityService legalEntityService;

    public boolean isLegalEntityExists(AddLegalEntityDTO legalEntityDTO) {
        String name = legalEntityDTO.getName();
        Integer unp = legalEntityDTO.getUnp();
        String UNP = null;
        if (unp != null) {
            UNP = unp.toString();
        }
        String ibanByByn = legalEntityDTO.getIbanByByn();
        if (name != null) {
            LegalEntityDTO legalEntity = legalEntityService.getByName(name);
            if (legalEntity != null) {
                return true;
            }
        }
        if (unp != null) {
            LegalEntityDTO legalEntity = legalEntityService.getByUnp(UNP);
            if (legalEntity != null) {
                return true;
            }
        }
        if (ibanByByn != null) {
            LegalEntityDTO legalEntity = legalEntityService.getByIbanByByn(ibanByByn);
            if (legalEntity != null) {
                return true;
            }
        }
        return false;
    }
}
