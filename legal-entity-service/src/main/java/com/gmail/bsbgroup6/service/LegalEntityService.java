package com.gmail.bsbgroup6.service;

import com.gmail.bsbgroup6.service.model.AddLegalEntityDTO;
import com.gmail.bsbgroup6.service.model.LegalEntityDTO;

import java.util.List;

public interface LegalEntityService {

    LegalEntityDTO add(AddLegalEntityDTO legalEntityDTO);

    List<LegalEntityDTO> getAll();

    LegalEntityDTO getById(Long id);

    LegalEntityDTO getByName(String name);

    LegalEntityDTO getByUnp(Integer unp);

    LegalEntityDTO getByIbanByByn(String ibanByByn);
}
