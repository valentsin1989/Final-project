package com.gmail.bsbgroup6.service.converter;

import com.gmail.bsbgroup6.repository.EmployeeServiceRepository;
import com.gmail.bsbgroup6.repository.LegalServiceRepository;
import com.gmail.bsbgroup6.repository.model.Application;
import com.gmail.bsbgroup6.repository.model.ApplicationDetails;
import com.gmail.bsbgroup6.service.model.AddApplicationDTO;
import com.gmail.bsbgroup6.service.model.AddedApplicationDTO;
import com.gmail.bsbgroup6.service.model.ApplicationDTO;
import com.gmail.bsbgroup6.service.model.EmployeeDTO;
import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import com.gmail.bsbgroup6.service.model.StatusEnum;
import com.gmail.bsbgroup6.service.model.ValueEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ApplicationConverter {

    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private final LegalServiceRepository legalServiceRepository;
    private final EmployeeServiceRepository employeeServiceRepository;

    public AddedApplicationDTO convertToAddedApplicationDTO(Application application) {
        AddedApplicationDTO applicationDTO = new AddedApplicationDTO();
        UUID uniqueNumber = application.getUniqueNumber();
        String uuid = uniqueNumber.toString();
        applicationDTO.setUniqueNumber(uuid);
        return applicationDTO;
    }

    public Application convertToApplication(AddApplicationDTO applicationDTO, String token) {
        Application application = new Application();
        String uniqueNumber = applicationDTO.getUniqueNumber();
        UUID uuid = UUID.fromString(uniqueNumber);
        application.setUniqueNumber(uuid);
        String status = StatusEnum.NEW.getValue();
        application.setStatus(status);
        String valueLegal = applicationDTO.getValueLegal();
        ValueEnum valueEnumLegal = ValueEnum.valueOf(valueLegal);
        Integer legalValue = valueEnumLegal.getValue();
        application.setValueLegal(legalValue);
        String valueIndividual = applicationDTO.getValueIndividual();
        ValueEnum valueEnumIndividual = ValueEnum.valueOf(valueIndividual);
        Integer individualValue = valueEnumIndividual.getValue();
        application.setValueIndividual(individualValue);
        Long employeeId = applicationDTO.getEmployeeId();
        application.setEmployeeId(employeeId);
        Float conversionPercentage = applicationDTO.getConversionPercentage();
        application.setConversionPercentage(conversionPercentage);
        String legalEntityName = applicationDTO.getLegalEntityName();
        List<LegalEntityDTO> legalEntities = legalServiceRepository.getLegalByName(legalEntityName, token);
        LegalEntityDTO legalEntityDTO = legalEntities.get(0);
        Long legalEntityId = legalEntityDTO.getId();
        application.setLegalEntityId(legalEntityId);
        return application;
    }

    public ApplicationDetails convertToApplicationDetails(Application applicationWithId) {
        ApplicationDetails applicationDetails = applicationWithId.getApplicationDetails();
        if (applicationDetails == null) {
            applicationDetails = new ApplicationDetails(applicationWithId);
            LocalDate localDate = LocalDate.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            String dateString = dateTimeFormatter.format(localDate);
            applicationDetails.setCreateDate(dateString);
            applicationDetails.setLastUpdate(dateString);
        }
        return applicationDetails;
    }

    public ApplicationDTO convertToApplicationDTO(Application application, String token) {
        UUID uniqueNumber = application.getUniqueNumber();
        String uuid = uniqueNumber.toString();
        String status = application.getStatus();
        Long employeeId = application.getEmployeeId();
        EmployeeDTO employee = employeeServiceRepository.getEmployeeById(employeeId, token);
        String fullName = employee.getFullName();
        Float conversionPercentage = application.getConversionPercentage();
        Integer valueLegal = application.getValueLegal();
        List<ValueEnum> valuesList = Arrays.asList(ValueEnum.values());
        ValueEnum legalValue = valuesList.stream()
                .filter(eachValue -> eachValue.getValue().equals(valueLegal))
                .findAny()
                .orElse(null);
        String valueLegalAsString = null;
        if (legalValue != null) {
            valueLegalAsString = legalValue.toString();
        }
        Integer valueIndividual = application.getValueIndividual();
        ValueEnum individualValue = valuesList.stream()
                .filter(eachValue -> eachValue.getValue().equals(valueIndividual))
                .findAny()
                .orElse(null);
        String valueIndividualAsString = null;
        if (individualValue != null) {
            valueIndividualAsString = individualValue.toString();
        }
        Long legalEntityId = application.getLegalEntityId();
        LegalEntityDTO legalEntity = legalServiceRepository.getLegalById(legalEntityId, token);
        String legalEntityName = legalEntity.getName();
        return ApplicationDTO.builder()
                .uniqueNumber(uuid)
                .status(status)
                .employeeId(employeeId)
                .employeeFullName(fullName)
                .conversionPercentage(conversionPercentage)
                .valueLegal(valueLegalAsString)
                .valueIndividual(valueIndividualAsString)
                .legalEntityName(legalEntityName)
                .build();
    }

    public List<ApplicationDTO> convertToListApplicationDTO(List<Application> applications, String token) {
        return applications.stream()
                .map((Application application) -> convertToApplicationDTO(application, token))
                .collect(Collectors.toList());
    }
}
