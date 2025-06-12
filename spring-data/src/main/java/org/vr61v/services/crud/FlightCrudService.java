package org.vr61v.services.crud;

import org.springframework.stereotype.Service;
import org.vr61v.entities.Flight;
import org.vr61v.repositories.FlightRepository;
import org.vr61v.services.CrudService;

@Service
public class FlightCrudService extends CrudService<Flight, Integer> {

    public FlightCrudService(FlightRepository repository) {
        super(repository);
    }

}
