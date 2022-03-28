package com.gmail.bsbgroup6.service.impl;

import com.gmail.bsbgroup6.repository.EmployeeDetailsRepository;
import com.gmail.bsbgroup6.repository.EmployeeRepository;
import com.gmail.bsbgroup6.repository.LegalServiceRepository;
import com.gmail.bsbgroup6.repository.model.Employee;
import com.gmail.bsbgroup6.repository.model.EmployeeDetails;
import com.gmail.bsbgroup6.repository.model.Pagination;
import com.gmail.bsbgroup6.service.converter.EmployeeConverter;
import com.gmail.bsbgroup6.service.exception.ServiceException;
import com.gmail.bsbgroup6.service.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeDetailsRepository employeeDetailsRepository;

    @Mock
    private EmployeeConverter employeeConverter;

    @Mock
    private LegalServiceRepository legalServiceRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void shouldReturnAddedEmployeeDTOWhenAddIfNotAdded() {
        String token = "token";
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO("name", LocalDate.now(), LocalDate.now(), "test", "test", "test");
        Employee employee = new Employee();
        when(employeeConverter.convertToEmployee(addEmployeeDTO, token)).thenReturn(employee);
        assertThrows(ServiceException.class, () -> employeeService.add(addEmployeeDTO, token));
    }

    @Test
    void shouldReturnLegalEntityDTOWhenAddIfAdded() {
        String token = "token";
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO("name", LocalDate.now(), LocalDate.now(), "test", "test", "test");
        Employee employee = new Employee();
        employee.setId(1L);
        EmployeeDetails employeeDetails = new EmployeeDetails();
        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        when(employeeConverter.convertToEmployee(addEmployeeDTO, token)).thenReturn(employee);
        when(employeeConverter.convertToEmployeeDetails(employee)).thenReturn(employeeDetails);
        when(employeeConverter.convertToAddedEmployeeDTO(employee)).thenReturn(addedEmployeeDTO);
        assertEquals(AddedEmployeeDTO.class, employeeService.add(addEmployeeDTO, token).getClass());
    }

    @Test
    void shouldReturnListEmployeeDTOWhenGetByPaginationIfPaginationNotValid() {
        String token = "token";
        PaginationEmployeeDTO paginationLegalEntityDTO = new PaginationEmployeeDTO();
        paginationLegalEntityDTO.setPagination(PaginationEnum.MANUAL);
        assertThrows(ServiceException.class, () -> employeeService.getByPagination(paginationLegalEntityDTO, token));
    }

    @Test
    void shouldReturnListEmployeeDTOWhenGetByPaginationIfPaginationIsDefaultOrCustom() {
        String token = "token";
        PaginationEmployeeDTO paginationEmployeeDTO = new PaginationEmployeeDTO();
        paginationEmployeeDTO.setPagination(PaginationEnum.DEFAULT);
        paginationEmployeeDTO.setPage(1);
        paginationEmployeeDTO.setCustomizedPage(20);
        Pagination pagination = new Pagination();
        pagination.setPage(paginationEmployeeDTO.getPage());
        pagination.setMaxResult(10);
        List<Employee> employeeList = new ArrayList<>();
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        when(employeeRepository.findByPagination(pagination)).thenReturn(employeeList);
        when(employeeConverter.convertToListEmployeeDTO(employeeList, token)).thenReturn(employeeDTOList);
        assertEquals(ArrayList.class, employeeService.getByPagination(paginationEmployeeDTO, token).getClass());
    }

    @Test
    void shouldReturnEmployeeDTOWhenGetByIdIfEmployeeNotExist() {
        String token = "token";
        Long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(null);
        assertNull(employeeService.getById(id, token));
    }

    @Test
    void shouldReturnEmployeeDTOWhenGetByIdIfEmployeeExist() {
        String token = "token";
        Long id = 1L;
        Employee employee = new Employee();
        EmployeeDTO employeeDTO = new EmployeeDTO(1l, "", "", "", "","", "");
        when(employeeRepository.findById(id)).thenReturn(employee);
        when(employeeConverter.convertToEmployeeDTO(employee, token)).thenReturn(employeeDTO);
        assertEquals(EmployeeDTO.class, employeeService.getById(id, token).getClass());
    }

    @Test
    void shouldReturnEmptyListWhenGetByParametersIfEmployeeNotExist() {
        String token = "token";
        SearchEmployeeDTO searchEmployeeDTO = new SearchEmployeeDTO();
        searchEmployeeDTO.setFullName("name");
        when(employeeRepository.findByFullName(searchEmployeeDTO.getFullName())).thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(), employeeService.getByParameters(searchEmployeeDTO, token));
    }

    @Test
    void shouldReturnAddedEmployeeDTOWhenGetByFullNameIfEmployeeNotExist() {
        String fullName = "name";
        when(employeeRepository.findByName(fullName)).thenReturn(Optional.empty());
        assertNull(employeeService.getByFullName(fullName));
    }

    @Test
    void shouldReturnAddedEmployeeDTOWhenGetByFullNameIfEmployeeExist() {
        String fullName = "name";
        Employee employee = new Employee();
        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        when(employeeRepository.findByName(fullName)).thenReturn(Optional.of(employee));
        when(employeeConverter.convertToAddedEmployeeDTO(employee)).thenReturn(addedEmployeeDTO);
        assertEquals(AddedEmployeeDTO.class, employeeService.getByFullName(fullName).getClass());
    }

    @Test
    void shouldReturnAddedEmployeeDTOWhenGetByPersonIbanBynIfEmployeeNotExist() {
        String ibanByByn = "test";
        when(employeeRepository.findByPersonIbanByn(ibanByByn)).thenReturn(Optional.empty());
        assertNull(employeeService.getByPersonIbanByn(ibanByByn));
    }

    @Test
    void shouldReturnAddedEmployeeDTOWhenGetByPersonIbanBynIfEmployeeExist() {
        String ibanByByn = "test";
        Employee employee = new Employee();
        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        when(employeeRepository.findByPersonIbanByn(ibanByByn)).thenReturn(Optional.of(employee));
        when(employeeConverter.convertToAddedEmployeeDTO(employee)).thenReturn(addedEmployeeDTO);
        assertEquals(AddedEmployeeDTO.class, employeeService.getByPersonIbanByn(ibanByByn).getClass());
    }

    @Test
    void shouldReturnAddedEmployeeDTOWhenGetByPersonIbanCurrencyIfEmployeeNotExist() {
        String personIbanCurrency = "test";
        when(employeeRepository.findByPersonIbanCurrency(personIbanCurrency)).thenReturn(Optional.empty());
        assertNull(employeeService.getByPersonIbanCurrency(personIbanCurrency));
    }

    @Test
    void shouldReturnAddedEmployeeDTOWhenGetByPersonIbanCurrencyIfEmployeeExist() {
        String personIbanCurrency = "test";
        Employee employee = new Employee();
        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        when(employeeRepository.findByPersonIbanCurrency(personIbanCurrency)).thenReturn(Optional.of(employee));
        when(employeeConverter.convertToAddedEmployeeDTO(employee)).thenReturn(addedEmployeeDTO);
        assertEquals(AddedEmployeeDTO.class, employeeService.getByPersonIbanCurrency(personIbanCurrency).getClass());
    }

    @Test
    void getByPersonIbanCurrency() {
    }
}