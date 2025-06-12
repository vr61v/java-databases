package org.vr61v.services.crud;

import org.springframework.stereotype.Service;
import org.vr61v.entities.Aircraft;
import org.vr61v.repositories.AircraftRepository;
import org.vr61v.services.CrudService;

@Service
public class AircraftCrudService extends CrudService<Aircraft, String> {

    public AircraftCrudService(AircraftRepository repository) {
        super(repository);
    }

}
