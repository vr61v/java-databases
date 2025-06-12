package org.vr61v.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vr61v.embedded.TicketFlightID;
import org.vr61v.types.FareCondition;

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
