package com.vr61v.entities;

import com.vr61v.entities.embedded.SeatID;
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
@Table(name = "seats", schema = "bookings")
public class Seat {

    @EmbeddedId
    private SeatID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "fare_conditions", length = 10, nullable = false)
    private FareCondition fareConditions;

}
