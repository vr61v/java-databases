package com.vr61v.entities;

import com.vr61v.entities.embedded.TicketFlightID;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ticket_flights", schema = "bookings")
public class TicketFlight {

    @EmbeddedId
    private TicketFlightID id;

    @Column(name = "fare_conditions", length = 10, nullable = false)
    private String fareConditions;

    @Column(name = "amount", nullable = false)
    private Float amount;

}
