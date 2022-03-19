package com.gmail.bsbgroup6.service.converter;

import com.gmail.bsbgroup6.repository.LegalServiceRepository;
import com.gmail.bsbgroup6.repository.model.Employee;
import com.gmail.bsbgroup6.service.model.AddEmployeeDTO;
import com.gmail.bsbgroup6.service.model.AddedEmployeeDTO;
import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import org.springframework.stereotype.Component;

@Component
public class EmployeeConverter {

    private final LegalServiceRepository legalServiceRepository;

    public EmployeeConverter(LegalServiceRepository legalServiceRepository) {
        this.legalServiceRepository = legalServiceRepository;
    }

    public Employee convertToEmployee(AddEmployeeDTO addEmployeeDTO) {
        Employee employee = new Employee();
        String fullName = addEmployeeDTO.getFullName();
        employee.setFullName(fullName);
        String recruitmentDate = addEmployeeDTO.getRecruitmentDate();
        employee.setRecruitmentDate(recruitmentDate);
        String terminationDate = addEmployeeDTO.getTerminationDate();
        employee.setTerminationDate(terminationDate);
        String legalEntityName = addEmployeeDTO.getLegalEntityName();
        LegalEntityDTO legalEntityDTO = legalServiceRepository.getLegalByName(legalEntityName);
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
}
