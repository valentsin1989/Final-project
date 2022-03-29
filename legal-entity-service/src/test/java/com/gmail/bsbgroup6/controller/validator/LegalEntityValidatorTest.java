package com.gmail.bsbgroup6.controller.validator;

import com.gmail.bsbgroup6.service.LegalEntityService;
import com.gmail.bsbgroup6.service.model.AddLegalEntityDTO;
import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class LegalEntityValidatorTest {

    @Mock
    private LegalEntityService legalEntityService;

    @InjectMocks
    private LegalEntityValidator legalEntityValidator;

    @Test
    void shouldReturnTruWhenIsLegalEntityExistsByNameIfExist() {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO("name", null, null, null, null);
        LegalEntityDTO legalEntityDTO = new LegalEntityDTO();
        when(legalEntityService.getByName(addLegalEntityDTO.getName())).thenReturn(legalEntityDTO);
        assertTrue(legalEntityValidator.isLegalEntityExists(addLegalEntityDTO));
    }

    @Test
    void shouldReturnTruWhenIsLegalEntityExistsByUnpIfExist() {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO(null, 1, null, null, null);
        LegalEntityDTO legalEntityDTO = new LegalEntityDTO();
        when(legalEntityService.getByUnp(addLegalEntityDTO.getUnp().toString())).thenReturn(legalEntityDTO);
        assertTrue(legalEntityValidator.isLegalEntityExists(addLegalEntityDTO));
    }

    @Test
    void shouldReturnTruWhenIsLegalEntityExistsByIbanBynIfExist() {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO(null, null, "1", null, null);
        LegalEntityDTO legalEntityDTO = new LegalEntityDTO();
        when(legalEntityService.getByIbanByByn(addLegalEntityDTO.getIbanByByn())).thenReturn(legalEntityDTO);
        assertTrue(legalEntityValidator.isLegalEntityExists(addLegalEntityDTO));
    }

    @Test
    void shouldReturnTruWhenIsLegalEntityExistsIfAllParametersNull() {
        AddLegalEntityDTO addLegalEntityDTO = new AddLegalEntityDTO(null, null, null, null, null);
        assertFalse(legalEntityValidator.isLegalEntityExists(addLegalEntityDTO));
    }
}