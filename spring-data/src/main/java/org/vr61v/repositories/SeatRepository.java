package org.vr61v.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.vr61v.embedded.SeatID;
import org.vr61v.entities.Seat;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, SeatID> {

    @Query("SELECT s FROM Seat s WHERE s.id.aircraft.aircraftCode = :aircraftCode")
    List<Seat> findSeatsByAircraftCode(String aircraftCode);


    @Modifying
    @Query(value = "DELETE FROM Seat s WHERE s.id.aircraft.aircraftCode = :aircraftCode")
    void deleteSeatsByAircraftCode(String aircraftCode);

}
