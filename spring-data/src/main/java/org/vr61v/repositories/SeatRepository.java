package org.vr61v.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vr61v.entities.Seat;
import org.vr61v.embedded.SeatID;

@Repository
public interface SeatRepository
        extends JpaRepository<Seat, SeatID> {

}
