package com.vr61v.repositories.impl;

import com.vr61v.entities.Ticket;
import com.vr61v.repositories.Repository;
import com.vr61v.utils.RepositorySessionManager;

public class TicketRepository extends Repository<Ticket, String> {

    public TicketRepository(RepositorySessionManager sessionManager) {
        super(sessionManager, Ticket.class);
    }

}
