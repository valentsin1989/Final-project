package com.gmail.bsbgroup6.repository;

import com.gmail.bsbgroup6.repository.model.Application;
import com.gmail.bsbgroup6.repository.model.Pagination;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository extends GenericRepository<Long, Application> {

    Optional<Application> findByUUID(UUID uniqueNumber);

    List<Application> findByPagination(Pagination pagination);
}
