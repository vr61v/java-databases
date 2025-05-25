package com.vr61v.entities;

import com.vr61v.entities.embedded.SeatID;
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
@Table(name = "seats", schema = "bookings")
public class Seat {

    @EmbeddedId
    private SeatID id;

    @Column(name = "fare_conditions", length = 10, nullable = false)
    private String fareConditions;

}
