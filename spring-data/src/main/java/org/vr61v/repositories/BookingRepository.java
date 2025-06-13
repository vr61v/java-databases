package org.vr61v.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vr61v.entities.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> { }
