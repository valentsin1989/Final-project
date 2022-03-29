package com.gmail.bsbgroup6.service.converter;

import com.gmail.bsbgroup6.repository.model.LegalEntity;
import com.gmail.bsbgroup6.repository.model.LegalEntityDetails;
import com.gmail.bsbgroup6.service.model.AddLegalEntityDTO;
import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class LegalEntityConverter {

    private static final String DATE_PATTERN = "dd/MM/yyyy";

    public LegalEntity convertToLegalEntity(AddLegalEntityDTO legalEntityDTO) {
        LegalEntity legalEntity = new LegalEntity();
        String name = legalEntityDTO.getName();
        legalEntity.setName(name);
        Integer UNP = legalEntityDTO.getUnp();
        String unp = UNP.toString();
        legalEntity.setUnp(unp);
        String IBANbyBYN = legalEntityDTO.getIbanByByn();
        legalEntity.setIbanByByn(IBANbyBYN);
        String legalType = legalEntityDTO.getType().toString();
        legalEntity.setType(legalType);
        Integer totalEmployees = legalEntityDTO.getTotalEmployees();
        legalEntity.setTotalEmployees(totalEmployees);
        return legalEntity;
    }

    public LegalEntityDTO convertToLegalEntityDTO(LegalEntity legalEntity) {
        Long id = legalEntity.getId();
        String name = legalEntity.getName();
        String unp = legalEntity.getUnp();
        Integer UNP = Integer.valueOf(unp);
        String IBANbyBYN = legalEntity.getIbanByByn();
        String type = legalEntity.getType();
        Integer totalEmployees = legalEntity.getTotalEmployees();
        LegalEntityDTO legalEntityDTO = new LegalEntityDTO();
        legalEntityDTO.setId(id);
        legalEntityDTO.setName(name);
        legalEntityDTO.setUnp(UNP);
        legalEntityDTO.setIbanByByn(IBANbyBYN);
        legalEntityDTO.setType(type);
        legalEntityDTO.setTotalEmployees(totalEmployees);
        return legalEntityDTO;
    }

    public List<LegalEntityDTO> convertToListLegalEntityDTO(List<LegalEntity> legalEntities) {
        return legalEntities.stream()
                .map(this::convertToLegalEntityDTO)
                .collect(Collectors.toList());
    }

    public LegalEntityDetails convertToLegalEntityDetails(LegalEntity legalEntityWithId) {
        LegalEntityDetails legalEntityDetails = legalEntityWithId.getLegalEntityDetails();
        if (legalEntityDetails == null) {
            legalEntityDetails = new LegalEntityDetails(legalEntityWithId);
            LocalDate localDate = LocalDate.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            String dateString = dateTimeFormatter.format(localDate);
            legalEntityDetails.setCreateDate(dateString);
            legalEntityDetails.setLastUpdate(dateString);
        }
        return legalEntityDetails;
    }
}
