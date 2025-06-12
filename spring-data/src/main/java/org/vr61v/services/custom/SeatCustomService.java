package org.vr61v.services.custom;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.vr61v.entities.Seat;
import org.vr61v.repositories.SeatRepository;

import java.util.List;

@Service
@Transactional
public class SeatCustomService {

    private final SeatRepository seatRepository;


    public SeatCustomService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> findSeatsByAircraftCode(String aircraftCode) {
        return seatRepository.findSeatsByAircraftCode(aircraftCode);
    }

    public void deleteSeatsByAircraftCode(String aircraftCode) {
        seatRepository.deleteSeatsByAircraftCode(aircraftCode);
    }

}
