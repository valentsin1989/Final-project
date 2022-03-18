package com.gmail.bsbgroup6.service.model;

import lombok.Data;

@Data
public class PaginationLegalEntityDTO {
    PaginationEnum pagination;
    Integer page;
    Integer customizedPage;
}
