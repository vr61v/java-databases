package org.vr61v.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.vr61v.embedded.LocalizedString;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"seats", "flights"})
@EqualsAndHashCode(exclude = {"seats", "flights"})
@Builder
@Entity
@Table(name = "aircrafts_data", schema = "bookings")
public class Aircraft {

    @Id
    @Column(name = "aircraft_code", nullable = false)
    private String aircraftCode;

    @Embedded
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "model", nullable = false)
    private LocalizedString model;

    @Column(name = "range", nullable = false)
    private Integer range;

    @OneToMany(mappedBy = "id.aircraft", fetch = FetchType.LAZY)
    private Set<Seat> seats;

    @OneToMany(mappedBy = "aircraft", fetch = FetchType.LAZY)
    private Set<Flight> flights;

}
