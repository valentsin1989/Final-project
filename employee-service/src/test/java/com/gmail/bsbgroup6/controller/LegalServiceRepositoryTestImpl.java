package com.gmail.bsbgroup6.controller;

import com.gmail.bsbgroup6.repository.LegalServiceRepository;
import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class LegalServiceRepositoryTestImpl implements LegalServiceRepository {
    @Override
    public List<LegalEntityDTO> getLegalByName(String name, String token) {
        List<LegalEntityDTO> legals = new ArrayList<>();
        LegalEntityDTO legalEntityDTO = new LegalEntityDTO();
        legalEntityDTO.setId(1L);
        legalEntityDTO.setName("Test Name");
        legalEntityDTO.setUnp(123456789);
        legalEntityDTO.setIbanByByn("BY00UNBS00000000000000000000");
        legalEntityDTO.setType("RESIDENT");
        legalEntityDTO.setTotalEmployees(100);
        legals.add(legalEntityDTO);
        return legals;
    }

    @Override
    public LegalEntityDTO getLegalById(Long LegalId, String token) {
        return null;
    }

    @Override
    public List<LegalEntityDTO> getLegalByNameAndUnp(String name, String unp, String token) {
        return null;
    }
}
