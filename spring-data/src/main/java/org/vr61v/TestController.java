package org.vr61v;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vr61v.services.crud.AircraftCrudService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TestController {

    private final AircraftCrudService service;

    @GetMapping
    public String hello() {
        return service.findAll().toString();
    }

}
