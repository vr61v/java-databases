package org.vr61v.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.vr61v.entities.embedded.LocalizedString;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"departures", "arrivals"})
@EqualsAndHashCode(exclude = {"departures", "arrivals"})
@Builder
@Entity
@Table(name = "airports_data", schema = "bookings")
public class Airport {

    @Id
    @Column(name = "airport_code", length = 3, nullable = false)
    private String airportCode;

    @Embedded
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "airport_name", nullable = false)
    private LocalizedString airportName;

    @Embedded
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "city", nullable = false)
    private LocalizedString city;

    @Column(name = "timezone", nullable = false)
    private String timezone;

    @OneToMany(mappedBy = "arrivalAirport", fetch = FetchType.LAZY)
    private Set<Flight> arrivals;

    @OneToMany(mappedBy = "departureAirport", fetch = FetchType.LAZY)
    private Set<Flight> departures;

}
