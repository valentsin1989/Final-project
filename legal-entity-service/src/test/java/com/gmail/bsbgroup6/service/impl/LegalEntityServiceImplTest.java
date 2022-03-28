package com.gmail.bsbgroup6.service.impl;

import com.gmail.bsbgroup6.repository.LegalEntityDatesRepository;
import com.gmail.bsbgroup6.repository.LegalEntityRepository;
import com.gmail.bsbgroup6.repository.RedisRepository;
import com.gmail.bsbgroup6.repository.model.LegalEntity;
import com.gmail.bsbgroup6.repository.model.LegalEntityDates;
import com.gmail.bsbgroup6.repository.model.LegalSearch;
import com.gmail.bsbgroup6.repository.model.Pagination;
import com.gmail.bsbgroup6.security.util.JwtUtils;
import com.gmail.bsbgroup6.service.converter.LegalEntityConverter;
import com.gmail.bsbgroup6.service.exception.ServiceException;
import com.gmail.bsbgroup6.service.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LegalEntityServiceImplTest {

    @Mock
    private LegalEntityRepository legalEntityRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private RedisRepository redisRepository;

    @Mock
    private LegalEntityConverter legalEntityConverter;

    @Mock
    private LegalEntityDatesRepository legalEntityDatesRepository;

    @InjectMocks
    private LegalEntityServiceImpl legalEntityService;

    @Test
    void shouldReturnLegalEntityDTOWhenAddIfNotAdded() {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO("name", 1, "1", LegalTypeEnum.NO_RESIDENT, 1);
        LegalEntity legalEntity = new LegalEntity();
        when(legalEntityConverter.convertToLegalEntity(addLegalEntityDTO)).thenReturn(legalEntity);
        assertThrows(ServiceException.class, () -> legalEntityService.add(addLegalEntityDTO));
    }

    @Test
    void shouldReturnLegalEntityDTOWhenAddIfAdded() {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO("name", 1, "1", LegalTypeEnum.NO_RESIDENT, 1);
        LegalEntity legalEntity = new LegalEntity();
        legalEntity.setId(1L);
        LegalEntityDates legalEntityDates = new LegalEntityDates();
        LegalEntityDTO legalEntityDTO = new LegalEntityDTO();
        when(legalEntityConverter.convertToLegalEntity(addLegalEntityDTO)).thenReturn(legalEntity);
        when(legalEntityConverter.convertToLegalEntityDates(legalEntity)).thenReturn(legalEntityDates);
        when(legalEntityConverter.convertToLegalEntityDTO(legalEntity)).thenReturn(legalEntityDTO);
        assertEquals(LegalEntityDTO.class, legalEntityService.add(addLegalEntityDTO).getClass());
    }

    @Test
    void shouldReturnListLegalEntityDTOWhenGetByPaginationIfPaginationNotValid() {
        PaginationLegalEntityDTO paginationLegalEntityDTO = new PaginationLegalEntityDTO();
        paginationLegalEntityDTO.setPagination(PaginationEnum.MANUAL);
        assertThrows(ServiceException.class, () -> legalEntityService.getByPagination(paginationLegalEntityDTO));
    }

    @Test
    void shouldReturnListLegalEntityDTOWhenGetByPaginationIfPaginationIsDefaultOrCustom() {
        PaginationLegalEntityDTO paginationLegalEntityDTO = new PaginationLegalEntityDTO();
        paginationLegalEntityDTO.setPagination(PaginationEnum.DEFAULT);
        paginationLegalEntityDTO.setPage(1);
        paginationLegalEntityDTO.setCustomizedPage(20);
        Pagination pagination = new Pagination();
        pagination.setPage(paginationLegalEntityDTO.getPage());
        pagination.setMaxResult(10);
        List<LegalEntity> legalEntityList = new ArrayList<>();
        List<LegalEntityDTO> legalEntityDTOList = new ArrayList<>();
        when(legalEntityRepository.findByPagination(pagination)).thenReturn(legalEntityList);
        when(legalEntityConverter.convertToListLegalEntityDTO(legalEntityList)).thenReturn(legalEntityDTOList);
        assertEquals(ArrayList.class, legalEntityService.getByPagination(paginationLegalEntityDTO).getClass());
    }

    @Test
    void shouldReturnListLegalEntityDTOWhenGetByParameters() {
        SearchLegalEntityDTO searchLegalEntityDTO = new SearchLegalEntityDTO();
        LegalSearch legalSearch = new LegalSearch();
        LegalEntity legalEntity = new LegalEntity();
        LegalEntityDTO legalEntityDTO = new LegalEntityDTO();
        List<LegalEntity> legalEntityList = new ArrayList<>();
        legalEntityList.add(legalEntity);
        legalEntityList.add(legalEntity);
        when(legalEntityRepository.findByParameters(legalSearch)).thenReturn(legalEntityList);
        when(legalEntityConverter.convertToLegalEntityDTO(legalEntity)).thenReturn(legalEntityDTO);
        assertEquals(ArrayList.class, legalEntityService.getByParameters(searchLegalEntityDTO).getClass());
    }

    @Test
    void shouldReturnLegalEntityDTOWhenGetByIdIfUserNotExist() {
        Long id = 1L;
        when(legalEntityRepository.findById(id)).thenReturn(null);
        assertNull(legalEntityService.getById(id));
    }

    @Test
    void shouldReturnLegalEntityDTOWhenGetByIdIfUserExist() {
        Long id = 1L;
        LegalEntity legalEntity = new LegalEntity();
        LegalEntityDTO legalEntityDTO = new LegalEntityDTO();
        when(legalEntityRepository.findById(id)).thenReturn(legalEntity);
        when(legalEntityConverter.convertToLegalEntityDTO(legalEntity)).thenReturn(legalEntityDTO);
        assertEquals(LegalEntityDTO.class, legalEntityService.getById(id).getClass());
    }


    @Test
    void shouldReturnLegalEntityDTOWhenGetByNameIfUserNotExist() {
        String name = "name";
        when(legalEntityRepository.findByName(name)).thenReturn(Optional.empty());
        assertNull(legalEntityService.getByName(name));
    }

    @Test
    void shouldReturnLegalEntityDTOWhenGetByNameIfUserExist() {
        String name = "name";
        LegalEntity legalEntity = new LegalEntity();
        LegalEntityDTO legalEntityDTO = new LegalEntityDTO();
        when(legalEntityRepository.findByName(name)).thenReturn(Optional.of(legalEntity));
        when(legalEntityConverter.convertToLegalEntityDTO(legalEntity)).thenReturn(legalEntityDTO);
        assertEquals(LegalEntityDTO.class, legalEntityService.getByName(name).getClass());
    }

    @Test
    void shouldReturnLegalEntityDTOWhenGetByUnpIfUserNotExist() {
        String unp = "100000000";
        when(legalEntityRepository.findByUnp(unp)).thenReturn(Optional.empty());
        assertNull(legalEntityService.getByUnp(unp));
    }

    @Test
    void shouldReturnLegalEntityDTOWhenGetByUnpIfUserExist() {
        String unp = "100000000";
        LegalEntity legalEntity = new LegalEntity();
        LegalEntityDTO legalEntityDTO = new LegalEntityDTO();
        when(legalEntityRepository.findByUnp(unp)).thenReturn(Optional.of(legalEntity));
        when(legalEntityConverter.convertToLegalEntityDTO(legalEntity)).thenReturn(legalEntityDTO);
        assertEquals(LegalEntityDTO.class, legalEntityService.getByUnp(unp).getClass());
    }

    @Test
    void shouldReturnLegalEntityDTOWhenGetByIbanByBynIfUserNotExist() {
        String ibanByByn = "BY00UNBS0000000000000000000";
        when(legalEntityRepository.findByIbanByByn(ibanByByn)).thenReturn(Optional.empty());
        assertNull(legalEntityService.getByIbanByByn(ibanByByn));
    }

    @Test
    void shouldReturnLegalEntityDTOWhenGetByIbanByBynIfUserExist() {
        String ibanByByn = "BY00UNBS0000000000000000000";
        LegalEntity legalEntity = new LegalEntity();
        LegalEntityDTO legalEntityDTO = new LegalEntityDTO();
        when(legalEntityRepository.findByIbanByByn(ibanByByn)).thenReturn(Optional.of(legalEntity));
        when(legalEntityConverter.convertToLegalEntityDTO(legalEntity)).thenReturn(legalEntityDTO);
        assertEquals(LegalEntityDTO.class, legalEntityService.getByIbanByByn(ibanByByn).getClass());
    }
}