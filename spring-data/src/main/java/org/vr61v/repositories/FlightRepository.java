package org.vr61v.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vr61v.entities.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> { }
