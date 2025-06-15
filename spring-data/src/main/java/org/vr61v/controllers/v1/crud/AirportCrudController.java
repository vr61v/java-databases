package org.vr61v.controllers.v1.crud;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vr61v.controllers.v1.CrudController;
import org.vr61v.dtos.AirportDto;
import org.vr61v.entities.Airport;
import org.vr61v.mappers.AirportMapper;
import org.vr61v.services.crud.AirportCrudService;

@RestController
@RequestMapping("/api/v1/airports")
public class AirportCrudController extends CrudController<Airport, AirportDto, String> {

    public AirportCrudController(
            AirportCrudService airportCrudService,
            AirportMapper airportMapper
    ) {
        super(airportCrudService, airportMapper, "airport");
    }

    @Override
    protected void setId(Airport entity, String id) {
        entity.setAirportCode(id);
    }

    @Override
    protected String getId(Airport entity) {
        return entity.getAirportCode();
    }

}
