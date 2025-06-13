package org.vr61v.controllers.v1.crud;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vr61v.controllers.v1.CrudController;
import org.vr61v.dtos.AircraftDto;
import org.vr61v.entities.Aircraft;
import org.vr61v.mappers.AircraftMapper;
import org.vr61v.services.crud.AircraftCrudService;

@RestController
@RequestMapping("/api/v1/aircrafts")
public class AircraftCrudController extends CrudController<Aircraft, AircraftDto, String> {

    public AircraftCrudController(
            AircraftCrudService aircraftCrudService,
            AircraftMapper aircraftMapper
    ) {
        super(aircraftCrudService, aircraftMapper, "aircraft");
    }

    @Override
    protected void setId(Aircraft entity, String id) {
        entity.setAircraftCode(id);
    }

    @Override
    protected String getId(Aircraft entity) {
        return entity.getAircraftCode();
    }

}
