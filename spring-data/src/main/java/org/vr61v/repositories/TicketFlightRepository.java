package org.vr61v.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vr61v.embedded.TicketFlightID;
import org.vr61v.entities.TicketFlight;

@Repository
public interface TicketFlightRepository extends JpaRepository<TicketFlight, TicketFlightID> { }
