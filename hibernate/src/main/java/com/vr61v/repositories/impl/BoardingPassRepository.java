package com.vr61v.repositories.impl;

import com.vr61v.entities.BoardingPass;
import com.vr61v.entities.embedded.TicketFlightID;
import com.vr61v.repositories.Repository;
import com.vr61v.utils.RepositorySessionManager;

public class BoardingPassRepository extends Repository<BoardingPass, TicketFlightID> {

    public BoardingPassRepository(RepositorySessionManager sessionManager) {
        super(sessionManager, BoardingPass.class);
    }

}
