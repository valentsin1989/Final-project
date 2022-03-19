package com.gmail.bsbgroup6.repository;

import com.gmail.bsbgroup6.service.model.LegalEntityDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${feign.legal.name}", url = "${feign.legal.url}")
public interface LegalServiceRepository {

    @GetMapping(value = "/api/legals/", consumes = "application/json")
    LegalEntityDTO getLegalByName(@RequestParam(name = "Name_Legal") String name);

    @GetMapping(value = "/api/legals/{LegalId}", consumes = "application/json")
    LegalEntityDTO getLegalById(@PathVariable Long LegalId);
}
