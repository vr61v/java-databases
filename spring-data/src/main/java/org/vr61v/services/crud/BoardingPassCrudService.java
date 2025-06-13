package org.vr61v.services.crud;

import org.springframework.stereotype.Service;
import org.vr61v.embedded.TicketFlightID;
import org.vr61v.entities.BoardingPass;
import org.vr61v.repositories.BoardingPassRepository;
import org.vr61v.services.CrudService;

@Service
public class BoardingPassCrudService extends CrudService<BoardingPass, TicketFlightID> {

    public BoardingPassCrudService(BoardingPassRepository repository) {
        super(repository);
    }

}
