package com.vr61v.entities;

import com.vr61v.entities.embedded.TicketFlightID;
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
@Table(name = "boarding_passes", schema = "bookings")
public class BoardingPass {

    @EmbeddedId
    private TicketFlightID id;

    @Column(name = "boarding_no", nullable = false)
    private Integer boardingNo;

    @Column(name = "seat_no", length = 4, nullable = false)
    private String seatNo;

}
