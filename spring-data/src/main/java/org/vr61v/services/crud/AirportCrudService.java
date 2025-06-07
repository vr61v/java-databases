package org.vr61v.services.crud;

import org.springframework.stereotype.Service;
import org.vr61v.entities.Airport;
import org.vr61v.repositories.AirportRepository;
import org.vr61v.services.CrudService;

@Service
public class AirportCrudService extends CrudService<Airport, String> {

    public AirportCrudService(AirportRepository repository) {
        super(repository);
    }

}
