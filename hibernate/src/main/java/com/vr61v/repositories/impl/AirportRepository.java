package com.vr61v.repositories.impl;

import com.vr61v.entities.Airport;
import com.vr61v.repositories.Repository;
import com.vr61v.utils.RepositorySessionManager;

public class AirportRepository extends Repository<Airport, String> {

    public AirportRepository(RepositorySessionManager sessionManager) {
        super(sessionManager, Airport.class);
    }

}
