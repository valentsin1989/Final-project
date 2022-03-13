package com.gmail.bsbgroup6.service.converter;

import com.gmail.bsbgroup6.repository.model.LegalEntity;
import com.gmail.bsbgroup6.repository.model.LegalEntityDates;
import com.gmail.bsbgroup6.repository.model.LegalTypeEnum;
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

    public LegalEntity convertToLegalEntity(AddLegalEntityDTO legalEntityDTO) {
        LegalEntity legalEntity = new LegalEntity();
        String name = legalEntityDTO.getName();
        legalEntity.setName(name);
        Integer UNP = legalEntityDTO.getUnp();
        legalEntity.setUnp(UNP);
        String IBANbyBYN = legalEntityDTO.getIbanByByn();
        legalEntity.setIbanByByn(IBANbyBYN);
        Boolean type = legalEntityDTO.getType();
        if (type) {
            legalEntity.setType(LegalTypeEnum.RESIDENT.name());
        } else {
            legalEntity.setType(LegalTypeEnum.NO_RESIDENT.name());
        }
        Integer totalEmployees = legalEntityDTO.getTotalEmployees();
        legalEntity.setTotalEmployees(totalEmployees);
        return legalEntity;
    }

    public LegalEntityDTO convertToLegalEntityDTO(LegalEntity legalEntity) {
        Long id = legalEntity.getId();
        String name = legalEntity.getName();
        Integer UNP = legalEntity.getUnp();
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

    public LegalEntityDates convertToLegalEntityDates(Long id) {
        LegalEntityDates legalEntityDates = new LegalEntityDates();
        legalEntityDates.setLegalId(id);
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateString = dateTimeFormatter.format(localDate);
        legalEntityDates.setCreateDate(dateString);
        legalEntityDates.setLastUpdate(dateString);
        return legalEntityDates;
    }
}
