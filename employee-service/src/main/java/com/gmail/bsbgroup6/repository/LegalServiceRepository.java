package com.gmail.bsbgroup6.repository;

import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "${feign.legal.name}", url = "${feign.legal.url}")
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
}
