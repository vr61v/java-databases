package com.vr61v.repositories;

import com.vr61v.entities.Aircraft;
import com.vr61v.utils.RepositorySessionManager;

public class AircraftRepository extends Repository<Aircraft, String> {

    public AircraftRepository(RepositorySessionManager sessionManager) {
        super(sessionManager, Aircraft.class);
    }

}
