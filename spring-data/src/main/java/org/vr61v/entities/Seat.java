package org.vr61v.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vr61v.embedded.SeatID;
import org.vr61v.types.FareCondition;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "seats", schema = "bookings")
public class Seat {

    @EmbeddedId
    private SeatID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "fare_conditions", length = 10, nullable = false)
    private FareCondition fareConditions;

}
