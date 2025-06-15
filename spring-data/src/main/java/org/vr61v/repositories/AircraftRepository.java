package org.vr61v.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vr61v.entities.Aircraft;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, String> { }
