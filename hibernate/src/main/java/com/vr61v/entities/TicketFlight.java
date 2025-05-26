package com.vr61v.entities;

import com.vr61v.entities.embedded.TicketFlightID;
import com.vr61v.entities.types.FareCondition;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "fare_conditions", length = 10, nullable = false)
    private FareCondition fareConditions;

    @Column(name = "amount", nullable = false)
    private Float amount;

}
