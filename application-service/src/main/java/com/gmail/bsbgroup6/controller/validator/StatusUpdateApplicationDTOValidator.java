package com.gmail.bsbgroup6.controller.validator;

import com.gmail.bsbgroup6.repository.ApplicationRepository;
import com.gmail.bsbgroup6.repository.model.Application;
import com.gmail.bsbgroup6.service.model.StatusEnum;
import com.gmail.bsbgroup6.service.model.StatusUpdateApplicationDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class StatusUpdateApplicationDTOValidator {

    private final ApplicationRepository applicationRepository;

    public boolean isValidStatus(StatusUpdateApplicationDTO applicationDTO) {
        String uniqueNumber = applicationDTO.getApplicationConvId();
        UUID uuid = UUID.fromString(uniqueNumber);
        Application application = applicationRepository.findByUUID(uuid).orElse(null);
        String applicationStatus = null;
        if (application != null) {
            applicationStatus = application.getStatus();
        }
        String status = applicationDTO.getStatus();
        if (status.equals(StatusEnum.NEW.getValue())) {
            return false;
        }
        if (status.equals(StatusEnum.IN_PROGRESS.getValue()) &&
                applicationStatus != null &&
                applicationStatus.equals(StatusEnum.DONE.getValue())) {
            return false;
        }
        if (status.equals(StatusEnum.IN_PROGRESS.getValue()) &&
                applicationStatus != null &&
                applicationStatus.equals(StatusEnum.REJECTED.getValue())) {
            return false;
        }
        if (status.equals(StatusEnum.DONE.getValue()) &&
                applicationStatus != null &&
                applicationStatus.equals(StatusEnum.REJECTED.getValue())) {
            return false;
        }
        if (status.equals(StatusEnum.DONE.getValue()) &&
                applicationStatus != null &&
                applicationStatus.equals(StatusEnum.NEW.getValue())) {
            return false;
        }
        if (status.equals(StatusEnum.REJECTED.getValue()) &&
                applicationStatus != null &&
                applicationStatus.equals(StatusEnum.IN_PROGRESS.getValue())) {
            return false;
        }
        if (status.equals(StatusEnum.REJECTED.getValue()) &&
                applicationStatus != null &&
                applicationStatus.equals(StatusEnum.DONE.getValue())) {
            return false;
        }
        return true;
    }
}
