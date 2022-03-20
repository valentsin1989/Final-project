package com.gmail.bsbgroup6.service.converter;

import com.gmail.bsbgroup6.repository.LegalServiceRepository;
import com.gmail.bsbgroup6.repository.model.Employee;
import com.gmail.bsbgroup6.repository.model.EmployeeDetails;
import com.gmail.bsbgroup6.repository.model.PositionByLegalEnum;
import com.gmail.bsbgroup6.service.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeConverter {

    private static final String DATE_PATTERN = "dd/MM/yyyy";

    private final LegalServiceRepository legalServiceRepository;

    public EmployeeConverter(LegalServiceRepository legalServiceRepository) {
        this.legalServiceRepository = legalServiceRepository;
    }

    public Employee convertToEmployee(AddEmployeeDTO addEmployeeDTO, String token) {
        Employee employee = new Employee();
        String fullName = addEmployeeDTO.getFullName();
        employee.setFullName(fullName);
        String recruitmentDate = addEmployeeDTO.getRecruitmentDate();
        employee.setRecruitmentDate(recruitmentDate);
        String terminationDate = addEmployeeDTO.getTerminationDate();
        employee.setTerminationDate(terminationDate);
        String legalEntityName = addEmployeeDTO.getLegalEntityName();
        List<LegalEntityDTO> legals = legalServiceRepository.getLegalByName(legalEntityName, token);
        LegalEntityDTO legalEntityDTO = legals.get(0);
        Long legalId = legalEntityDTO.getId();
        employee.setLegalEntityId(legalId);
        String personIbanByn = addEmployeeDTO.getPersonIbanByn();
        employee.setPersonIbanByn(personIbanByn);
        String personIbanCurrency = addEmployeeDTO.getPersonIbanCurrency();
        employee.setPersonIbanCurrency(personIbanCurrency);
        return employee;
    }

    public AddedEmployeeDTO convertToAddedEmployeeDTO(Employee employee) {
        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        Long id = employee.getId();
        addedEmployeeDTO.setId(id);
        return addedEmployeeDTO;
    }

    public EmployeeDetails convertToEmployeeDetails(Employee employeeWthId) {
        EmployeeDetails employeeDetails = employeeWthId.getEmployeeDetails();
        if (employeeDetails == null) {
            employeeDetails = new EmployeeDetails(employeeWthId);
            LocalDate localDate = LocalDate.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            String dateString = dateTimeFormatter.format(localDate);
            employeeDetails.setCreateDate(dateString);
            employeeDetails.setLastUpdate(dateString);
            employeeDetails.setPositionByLegal(PositionByLegalEnum.HIRED.name());
        }
        return employeeDetails;
    }

    public EmployeeDTO convertToEmployeeDTO(Employee employee, String token) {
        Long id = employee.getId();
        String fullName = employee.getFullName();
        String recruitmentDate = employee.getRecruitmentDate();
        String terminationDate = employee.getTerminationDate();
        Long legalEntityId = employee.getLegalEntityId();
        LegalEntityDTO legalEntity = legalServiceRepository.getLegalById(legalEntityId, token);
        String legalEntityName = legalEntity.getName();
        String personIbanByn = employee.getPersonIbanByn();
        String personIbanCurrency = employee.getPersonIbanCurrency();
        return new EmployeeDTO(
                id,
                fullName,
                recruitmentDate,
                terminationDate,
                legalEntityName,
                personIbanByn,
                personIbanCurrency
        );
    }

    public List<EmployeeDTO> convertToListEmployeeDTO(List<Employee> employees, String token) {
        return employees.stream()
                .map((Employee employee) -> convertToEmployeeDTO(employee, token))
                .collect(Collectors.toList());
    }

    public GetEmployeeDTO convertToGetEmployeeDTO(Employee employee, String legalEntityName) {
        Long id = employee.getId();
        String recruitmentDate = employee.getRecruitmentDate();
        String terminationDate = employee.getTerminationDate();
        String personIbanByn = employee.getPersonIbanByn();
        String personIbanCurrency = employee.getPersonIbanCurrency();
        return new GetEmployeeDTO(
                id,
                recruitmentDate,
                terminationDate,
                legalEntityName,
                personIbanByn,
                personIbanCurrency
        );
    }
}
