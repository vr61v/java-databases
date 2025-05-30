package com.vr61v.repositories.impl;

import com.vr61v.entities.Booking;
import com.vr61v.repositories.Repository;
import com.vr61v.utils.RepositorySessionManager;

public class BookingRepository extends Repository<Booking, String> {

    public BookingRepository(RepositorySessionManager sessionManager) {
        super(sessionManager, Booking.class);
    }

}
