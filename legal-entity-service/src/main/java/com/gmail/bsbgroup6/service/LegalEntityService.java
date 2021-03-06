package com.gmail.bsbgroup6.service;

import com.gmail.bsbgroup6.service.model.AddLegalEntityDTO;
import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import com.gmail.bsbgroup6.service.model.PaginationLegalEntityDTO;
import com.gmail.bsbgroup6.service.model.SearchLegalEntityDTO;

import java.util.List;

public interface LegalEntityService {

    LegalEntityDTO add(AddLegalEntityDTO legalEntityDTO);

    List<LegalEntityDTO> getByPagination(PaginationLegalEntityDTO legalEntityDTO);

    List<LegalEntityDTO> getByParameters(SearchLegalEntityDTO legalEntityDTO);

    LegalEntityDTO getById(Long id);

    LegalEntityDTO getByName(String name);

    LegalEntityDTO getByUnp(String unp);

    LegalEntityDTO getByIbanByByn(String ibanByByn);
}
