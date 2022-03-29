package com.gmail.bsbgroup6.controller.validator;

import com.gmail.bsbgroup6.service.EmployeeService;
import com.gmail.bsbgroup6.service.model.AddEmployeeDTO;
import com.gmail.bsbgroup6.service.model.AddedEmployeeDTO;
import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeValidatorTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeValidator  employeeValidator;

    @Test
    void shouldReturnTruWhenIsLegalEntityExistsByNameIfExist() {
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO("name", null, null, null, null, null);
        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        when(employeeService.getByFullName(addEmployeeDTO.getFullName())).thenReturn(addedEmployeeDTO);
        assertTrue(employeeValidator.isEmployeeExists(addEmployeeDTO));
    }

    @Test
    void shouldReturnTruWhenIsLegalEntityExistsByUnpIfExist() {
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(null, null, null, null, "1", null);
        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        when(employeeService.getByPersonIbanByn(addEmployeeDTO.getPersonIbanByn())).thenReturn(addedEmployeeDTO);
        assertTrue(employeeValidator.isEmployeeExists(addEmployeeDTO));
    }

    @Test
    void shouldReturnTruWhenIsLegalEntityExistsByIbanBynIfExist() {
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(null, null, null, null, null, "1");
        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        when(employeeService.getByPersonIbanCurrency(addEmployeeDTO.getPersonIbanCurrency())).thenReturn(addedEmployeeDTO);
        assertTrue(employeeValidator.isEmployeeExists(addEmployeeDTO));
    }

    @Test
    void shouldReturnTruWhenIsLegalEntityExistsIfAllParametersNull() {
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO(null, null, null, null, null, null);
        assertFalse(employeeValidator.isEmployeeExists(addEmployeeDTO));
    }
}