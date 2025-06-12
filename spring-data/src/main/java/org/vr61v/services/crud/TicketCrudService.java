package org.vr61v.services.crud;

import org.springframework.stereotype.Service;
import org.vr61v.entities.Ticket;
import org.vr61v.repositories.TicketRepository;
import org.vr61v.services.CrudService;

@Service
public class TicketCrudService extends CrudService<Ticket, String> {

    public TicketCrudService(TicketRepository repository) {
        super(repository);
    }

}
