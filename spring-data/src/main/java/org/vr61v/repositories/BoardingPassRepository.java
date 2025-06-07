package org.vr61v.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vr61v.entities.BoardingPass;
import org.vr61v.entities.embedded.TicketFlightID;

@Repository
public interface BoardingPassRepository
        extends JpaRepository<BoardingPass, TicketFlightID> {

}
