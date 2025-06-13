package org.vr61v.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vr61v.entities.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> { }
