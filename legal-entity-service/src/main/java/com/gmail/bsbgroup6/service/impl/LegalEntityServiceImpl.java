package com.gmail.bsbgroup6.service.impl;

import com.gmail.bsbgroup6.repository.LegalEntityDetailsRepository;
import com.gmail.bsbgroup6.repository.LegalEntityRepository;
import com.gmail.bsbgroup6.repository.model.LegalEntity;
import com.gmail.bsbgroup6.repository.model.LegalEntityDetails;
import com.gmail.bsbgroup6.repository.model.LegalSearch;
import com.gmail.bsbgroup6.repository.model.Pagination;
import com.gmail.bsbgroup6.service.LegalEntityService;
import com.gmail.bsbgroup6.service.converter.LegalEntityConverter;
import com.gmail.bsbgroup6.service.exception.ServiceException;
import com.gmail.bsbgroup6.service.model.AddLegalEntityDTO;
import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import com.gmail.bsbgroup6.service.model.PaginationLegalEntityDTO;
import com.gmail.bsbgroup6.service.model.SearchLegalEntityDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LegalEntityServiceImpl implements LegalEntityService {

    private static final int DEFAULT_COUNT_OF_ENTITIES_PER_PAGE = 10;
    private final LegalEntityRepository legalEntityRepository;
    private final LegalEntityDetailsRepository legalEntityDetailsRepository;
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
        LegalEntityDetails legalEntityDetails = legalEntityConverter.convertToLegalEntityDetails(legalEntity);
        legalEntityDetailsRepository.add(legalEntityDetails);
        return legalEntityConverter.convertToLegalEntityDTO(legalEntity);
    }

    @Override
    @Transactional
    public List<LegalEntityDTO> getByPagination(PaginationLegalEntityDTO legalEntityDTO) {
        List<LegalEntity> legalEntities;
        switch (legalEntityDTO.getPagination()) {
            case DEFAULT: {
                Pagination pagination = new Pagination();
                pagination.setPage(legalEntityDTO.getPage());
                pagination.setMaxResult(DEFAULT_COUNT_OF_ENTITIES_PER_PAGE);
                legalEntities = legalEntityRepository.findByPagination(pagination);
                break;
            }
            case CUSTOMED: {
                Pagination pagination = new Pagination();
                pagination.setPage(legalEntityDTO.getPage());
                pagination.setMaxResult(legalEntityDTO.getCustomizedPage());
                legalEntities = legalEntityRepository.findByPagination(pagination);
                break;
            }
            default: {
                throw new ServiceException("Legal entities are not found.");
            }
        }
        return legalEntityConverter.convertToListLegalEntityDTO(legalEntities);
    }

    @Override
    @Transactional
    public List<LegalEntityDTO> getByParameters(SearchLegalEntityDTO legalEntityDTO) {
        String name = legalEntityDTO.getName();
        Integer unp = legalEntityDTO.getUnp();
        String UNP = null;
        if (unp != null) {
            UNP = unp.toString();
        }
        String ibanByByn = legalEntityDTO.getIbanByByn();
        LegalSearch legalSearch = new LegalSearch();
        legalSearch.setName(name);
        legalSearch.setUnp(UNP);
        legalSearch.setIbanByByn(ibanByByn);
        List<LegalEntity> legalEntities = legalEntityRepository.findByParameters(legalSearch);
        return legalEntities.stream()
                .map(legalEntityConverter::convertToLegalEntityDTO)
                .collect(Collectors.toList());
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
    public LegalEntityDTO getByUnp(String unp) {
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
