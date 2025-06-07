package org.vr61v.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vr61v.entities.TicketFlight;
import org.vr61v.embedded.TicketFlightID;

@Repository
public interface TicketFlightRepository
        extends JpaRepository<TicketFlight, TicketFlightID> {

}
