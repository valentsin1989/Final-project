package com.gmail.bsbgroup6.service;

import com.gmail.bsbgroup6.service.model.AddApplicationDTO;
import com.gmail.bsbgroup6.service.model.AddedApplicationDTO;
import com.gmail.bsbgroup6.service.model.ApplicationDTO;
import com.gmail.bsbgroup6.service.model.LegalUpdateApplicationDTO;
import com.gmail.bsbgroup6.service.model.PaginationApplicationDTO;
import com.gmail.bsbgroup6.service.model.StatusUpdateApplicationDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ApplicationService {

    List<AddApplicationDTO> getFromFile(MultipartFile file);

    AddedApplicationDTO getByUUID(String uniqueNumber);

    AddedApplicationDTO add(AddApplicationDTO applicationDTO, String token);

    List<ApplicationDTO> getByPagination(PaginationApplicationDTO applicationDTO, String token);

    ApplicationDTO getByUniqueNumber(UUID applicationConvId, String token);

    StatusUpdateApplicationDTO updateStatus(StatusUpdateApplicationDTO applicationDTO, String token);

    LegalUpdateApplicationDTO updateLegal(LegalUpdateApplicationDTO applicationDTO, String token);
}
