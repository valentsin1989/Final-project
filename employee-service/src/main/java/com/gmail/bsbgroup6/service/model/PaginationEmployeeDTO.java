package com.gmail.bsbgroup6.service.model;

import lombok.Data;

@Data
public class PaginationEmployeeDTO {
    PaginationEnum pagination;
    Integer page;
    Integer customizedPage;
}
