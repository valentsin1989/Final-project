package com.gmail.bsbgroup6.repository;

import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("${feign.service.legal}")
public interface LegalServiceRepository {

    @GetMapping(value = "/api/legals")
    List<LegalEntityDTO> getLegalByName(
            @RequestParam(name = "Name_Legal") String name,
            @RequestHeader(value = "Authorization") String token
    );

    @GetMapping(value = "/api/legals/{LegalId}")
    LegalEntityDTO getLegalById(
            @PathVariable Long LegalId,
            @RequestHeader(value = "Authorization") String token
    );

    @GetMapping(value = "/api/legals")
    List<LegalEntityDTO> getLegalByNameAndUnp(
            @RequestParam(name = "Name_Legal") String name,
            @RequestParam(name = "UNP") String unp,
            @RequestHeader(value = "Authorization") String token
    );
}
