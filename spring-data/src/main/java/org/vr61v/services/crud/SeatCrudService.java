package org.vr61v.services.crud;

import org.springframework.stereotype.Service;
import org.vr61v.embedded.SeatID;
import org.vr61v.entities.Seat;
import org.vr61v.repositories.SeatRepository;
import org.vr61v.services.CrudService;

@Service
public class SeatCrudService extends CrudService<Seat, SeatID> {

    public SeatCrudService(SeatRepository repository) {
        super(repository);
    }

}
