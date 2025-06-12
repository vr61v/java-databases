package org.vr61v.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vr61v.entities.Aircraft;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class SeatID {

    @ManyToOne
    @JoinColumn(name = "aircraft_code", nullable = false)
    private Aircraft aircraft;

    @Column(name = "seat_no", length = 4, nullable = false)
    private String seatNo;

}
