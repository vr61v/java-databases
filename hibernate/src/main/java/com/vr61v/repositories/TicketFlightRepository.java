package com.vr61v.repositories;

import com.vr61v.entities.TicketFlight;
import com.vr61v.entities.embedded.TicketFlightID;
import com.vr61v.utils.RepositorySessionManager;

public class TicketFlightRepository extends Repository<TicketFlight, TicketFlightID> {

    public TicketFlightRepository(RepositorySessionManager sessionManager) {
        super(sessionManager, TicketFlight.class);
    }

}
