package org.vr61v.entities.embedded;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vr61v.entities.Flight;
import org.vr61v.entities.Ticket;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class TicketFlightID {

    @ManyToOne
    @JoinColumn(name = "ticket_no", nullable = false)
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

}
