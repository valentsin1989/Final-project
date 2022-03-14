package com.gmail.bsbgroup6.service.impl;

import com.gmail.bsbgroup6.repository.LegalEntityDatesRepository;
import com.gmail.bsbgroup6.repository.LegalEntityRepository;
import com.gmail.bsbgroup6.repository.model.LegalEntity;
import com.gmail.bsbgroup6.repository.model.LegalEntityDates;
import com.gmail.bsbgroup6.service.LegalEntityService;
import com.gmail.bsbgroup6.service.converter.LegalEntityConverter;
import com.gmail.bsbgroup6.service.exception.ServiceException;
import com.gmail.bsbgroup6.service.model.AddLegalEntityDTO;
import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class LegalEntityServiceImpl implements LegalEntityService {

    private final LegalEntityRepository legalEntityRepository;
    private final LegalEntityDatesRepository legalEntityDatesRepository;
    private final LegalEntityConverter legalEntityConverter;

    @Override
    @Transactional
    public LegalEntityDTO add(AddLegalEntityDTO legalEntityDTO) {
        LegalEntity legalEntity = legalEntityConverter.convertToLegalEntity(legalEntityDTO);
        legalEntityRepository.add(legalEntity);
        Long id = legalEntity.getId();
        if (id == null) {
            throw new ServiceException("Legal entity wasn't added.");
        }
        LegalEntity legalEntityWithId = legalEntityRepository.findById(id);
        LegalEntityDates legalEntityDates = legalEntityConverter.convertToLegalEntityDates(legalEntityWithId);
        legalEntityDatesRepository.add(legalEntityDates);
        return legalEntityConverter.convertToLegalEntityDTO(legalEntity);
    }

    @Override
    @Transactional
    public List<LegalEntityDTO> getAll() {
        List<LegalEntity> legalEntities = legalEntityRepository.findAll();
        if (legalEntities == null) {
            throw new ServiceException("Legal entities are not found.");
        }
        return legalEntityConverter.convertToListLegalEntityDTO(legalEntities);
    }

    @Override
    @Transactional
    public LegalEntityDTO getById(Long id) {
        LegalEntity legalEntity = legalEntityRepository.findById(id);
        if (legalEntity == null) {
            return null;
        }
        return legalEntityConverter.convertToLegalEntityDTO(legalEntity);
    }

    @Override
    @Transactional
    public LegalEntityDTO getByName(String name) {
        LegalEntity legalEntity = legalEntityRepository.findByName(name).orElse(null);
        if (legalEntity == null) {
            return null;
        }
        return legalEntityConverter.convertToLegalEntityDTO(legalEntity);
    }

    @Override
    @Transactional
    public LegalEntityDTO getByUnp(Integer unp) {
        LegalEntity legalEntity = legalEntityRepository.findByUnp(unp).orElse(null);
        if (legalEntity == null) {
            return null;
        }
        return legalEntityConverter.convertToLegalEntityDTO(legalEntity);
    }

    @Override
    @Transactional
    public LegalEntityDTO getByIbanByByn(String ibanByByn) {
        LegalEntity legalEntity = legalEntityRepository.findByIbanByByn(ibanByByn).orElse(null);
        if (legalEntity == null) {
            return null;
        }
        return legalEntityConverter.convertToLegalEntityDTO(legalEntity);
    }
}
