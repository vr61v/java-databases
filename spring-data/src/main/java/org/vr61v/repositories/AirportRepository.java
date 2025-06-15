package org.vr61v.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vr61v.entities.Airport;

@Repository
public interface AirportRepository extends JpaRepository<Airport, String> { }
