package com.gmail.bsbgroup6.service.impl;

import com.gmail.bsbgroup6.repository.ApplicationDetailsRepository;
import com.gmail.bsbgroup6.repository.ApplicationRepository;
import com.gmail.bsbgroup6.repository.LegalServiceRepository;
import com.gmail.bsbgroup6.repository.model.Application;
import com.gmail.bsbgroup6.repository.model.ApplicationDetails;
import com.gmail.bsbgroup6.repository.model.Pagination;
import com.gmail.bsbgroup6.security.util.JwtUtils;
import com.gmail.bsbgroup6.service.ApplicationService;
import com.gmail.bsbgroup6.service.converter.ApplicationConverter;
import com.gmail.bsbgroup6.service.exception.ServiceException;
import com.gmail.bsbgroup6.service.model.AddApplicationDTO;
import com.gmail.bsbgroup6.service.model.AddedApplicationDTO;
import com.gmail.bsbgroup6.service.model.ApplicationDTO;
import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import com.gmail.bsbgroup6.service.model.LegalUpdateApplicationDTO;
import com.gmail.bsbgroup6.service.model.PaginationApplicationDTO;
import com.gmail.bsbgroup6.service.model.StatusUpdateApplicationDTO;
import com.gmail.bsbgroup6.service.model.UpdatedByStatusApplicationDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private static final String SEPARATOR_FOR_LINES = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    private static final int DEFAULT_COUNT_OF_ENTITIES_PER_PAGE = 10;
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private final ApplicationRepository applicationRepository;
    private final ApplicationDetailsRepository applicationDetailsRepository;
    private final LegalServiceRepository legalServiceRepository;
    private final ApplicationConverter applicationConverter;
    private final JwtUtils jwtUtils;

    @Override
    public List<AddApplicationDTO> getFromFile(MultipartFile file) {
        List<AddApplicationDTO> applications = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(convert(file)))) {
            String line = bufferedReader.readLine();
            int counter = 1;
            while (line != null) {
                if (counter == 1) {
                    counter++;
                    line = bufferedReader.readLine();
                    continue;
                }
                String[] applicationFields = line.split(SEPARATOR_FOR_LINES, -1);
                AddApplicationDTO applicationDTO = getApplicationDTO(applicationFields);
                applications.add(applicationDTO);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return applications;
    }

    @Override
    @Transactional
    public AddedApplicationDTO getByUUID(String uniqueNumber) {
        UUID uuid = UUID.fromString(uniqueNumber);
        Application application = applicationRepository.findByUUID(uuid).orElse(null);
        if (application == null) {
            return null;
        }
        return applicationConverter.convertToAddedApplicationDTO(application);
    }

    @Override
    @Transactional
    public AddedApplicationDTO add(AddApplicationDTO applicationDTO, String token) {
        Application application = applicationConverter.convertToApplication(applicationDTO, token);
        applicationRepository.add(application);
        Long id = application.getId();
        if (id == null) {
            throw new ServiceException("Application wasn't added");
        }
        ApplicationDetails applicationDetails = applicationConverter.convertToApplicationDetails(application);
        applicationDetailsRepository.add(applicationDetails);
        return applicationConverter.convertToAddedApplicationDTO(application);
    }

    @Override
    @Transactional
    public List<ApplicationDTO> getByPagination(PaginationApplicationDTO applicationDTO, String token) {
        List<Application> applications;
        switch (applicationDTO.getPagination()) {
            case DEFAULT: {
                Pagination pagination = new Pagination();
                pagination.setPage(applicationDTO.getPage());
                pagination.setMaxResult(DEFAULT_COUNT_OF_ENTITIES_PER_PAGE);
                applications = applicationRepository.findByPagination(pagination);
                break;
            }
            case CUSTOMED: {
                Pagination pagination = new Pagination();
                pagination.setPage(applicationDTO.getPage());
                pagination.setMaxResult(applicationDTO.getCustomizedPage());
                applications = applicationRepository.findByPagination(pagination);
                break;
            }
            default: {
                throw new ServiceException("Applications are not found.");
            }
        }
        return applicationConverter.convertToListApplicationDTO(applications, token);
    }

    @Override
    @Transactional
    public ApplicationDTO getByUniqueNumber(UUID applicationConvId, String token) {
        Application application = applicationRepository.findByUUID(applicationConvId).orElse(null);
        if (application == null) {
            return null;
        }
        return applicationConverter.convertToApplicationDTO(application, token);
    }

    @Override
    @Transactional
    public UpdatedByStatusApplicationDTO updateStatus(StatusUpdateApplicationDTO applicationDTO, String token) {
        String uniqueNumber = applicationDTO.getApplicationConvId();
        UUID uuid = UUID.fromString(uniqueNumber);
        String status = applicationDTO.getStatus();
        Application application = applicationRepository.findByUUID(uuid).orElse(null);
        if (application != null) {
            application.setStatus(status);
            ApplicationDetails applicationDetails = application.getApplicationDetails();
            LocalDate localDate = LocalDate.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            String dateString = dateTimeFormatter.format(localDate);
            applicationDetails.setLastUpdate(dateString);
            application.setApplicationDetails(applicationDetails);
        }
        Application updatedApplication = applicationRepository.update(application);
        UpdatedByStatusApplicationDTO updatedApplicationDTO = new UpdatedByStatusApplicationDTO();
        String updatedStatus = updatedApplication.getStatus();
        updatedApplicationDTO.setStatus(updatedStatus);
        String userName = jwtUtils.getUserNameFromJwtToken(token);
        updatedApplicationDTO.setUser(userName);
        return updatedApplicationDTO;
    }

    @Override
    @Transactional
    public LegalUpdateApplicationDTO updateLegal(LegalUpdateApplicationDTO applicationDTO, String token) {
        String legalEntityName = applicationDTO.getLegalEntityName();
        List<LegalEntityDTO> legalEntities = legalServiceRepository.getLegalByName(legalEntityName, token);
        LegalEntityDTO legalEntityDTO = legalEntities.get(0);
        if (!legalEntityDTO.getName().equals(legalEntityName)) {
            return null;
        }
        String applicationConvId = applicationDTO.getApplicationConvId();
        UUID uuid = UUID.fromString(applicationConvId);
        Application application = applicationRepository.findByUUID(uuid).orElse(null);
        Long legalEntityDTOId = legalEntityDTO.getId();
        if (application != null) {
            application.setLegalEntityId(legalEntityDTOId);
            ApplicationDetails applicationDetails = application.getApplicationDetails();
            LocalDate localDate = LocalDate.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            String dateString = dateTimeFormatter.format(localDate);
            applicationDetails.setLastUpdate(dateString);
            application.setApplicationDetails(applicationDetails);
        }
        applicationRepository.update(application);
        return applicationDTO;
    }

    private AddApplicationDTO getApplicationDTO(String[] applicationFields) {
        return AddApplicationDTO.builder()
                .uniqueNumber(applicationFields[0])
                .valueLegal(applicationFields[1])
                .valueIndividual(applicationFields[2])
                .employeeId(Long.valueOf(applicationFields[3]))
                .conversionPercentage(Float.valueOf(applicationFields[4]))
                .legalEntityName(applicationFields[5])
                .build();
    }

    public static File convert(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
