package com.vr61v.repositories;

import com.vr61v.entities.Booking;
import com.vr61v.utils.RepositorySessionManager;

public class BookingsRepository extends Repository<Booking, String> {

    public BookingsRepository(RepositorySessionManager sessionManager) {
        super(sessionManager, Booking.class);
    }
}
