package org.vr61v.controllers.v1.crud;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vr61v.controllers.v1.CrudController;
import org.vr61v.dtos.FlightDto;
import org.vr61v.entities.Flight;
import org.vr61v.mappers.FlightMapper;
import org.vr61v.services.crud.FlightCrudService;


@RestController
@RequestMapping("api/v1/flights")
public class FlightCrudController
        extends CrudController<Flight, FlightDto, Integer> {

    public FlightCrudController(
            FlightCrudService flightCrudService,
            FlightMapper flightMapper
    ) {
        super(flightCrudService, flightMapper);
    }

    @Override
    protected void setId(Flight entity, Integer id) {

    }

}
