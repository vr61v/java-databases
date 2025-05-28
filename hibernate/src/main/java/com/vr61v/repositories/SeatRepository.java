package com.vr61v.repositories;

import com.vr61v.entities.Seat;
import com.vr61v.entities.embedded.SeatID;
import com.vr61v.utils.RepositorySessionManager;

public class SeatRepository extends Repository<Seat, SeatID>{

    public SeatRepository(RepositorySessionManager sessionManager) {
        super(sessionManager, Seat.class);
    }

}
