package org.vr61v.services.crud;

import org.springframework.stereotype.Service;
import org.vr61v.embedded.TicketFlightID;
import org.vr61v.entities.TicketFlight;
import org.vr61v.repositories.TicketFlightRepository;
import org.vr61v.services.CrudService;

@Service
public class TicketFlightCrudService extends CrudService<TicketFlight, TicketFlightID> {

    public TicketFlightCrudService(TicketFlightRepository repository) {
        super(repository);
    }

}
