package org.vr61v.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vr61v.embedded.TicketFlightID;
import org.vr61v.entities.BoardingPass;

@Repository
public interface BoardingPassRepository extends JpaRepository<BoardingPass, TicketFlightID> { }
