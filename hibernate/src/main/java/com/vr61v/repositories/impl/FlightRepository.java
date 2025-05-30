package com.vr61v.repositories.impl;

import com.vr61v.entities.Flight;
import com.vr61v.repositories.Repository;
import com.vr61v.utils.RepositorySessionManager;

public class FlightRepository extends Repository<Flight, Integer> {

    public FlightRepository(RepositorySessionManager sessionManager) {
        super(sessionManager, Flight.class);
    }

}
