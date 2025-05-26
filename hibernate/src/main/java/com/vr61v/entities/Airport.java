package com.vr61v.entities;

import com.vr61v.entities.embedded.LocalizedString;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

/*
    todo: разобраться как можно парсить Point, пока что в табличке эта колонка должна быть удалена
     @Column(name = "coordinates", columnDefinition = "Point", nullable = false)
     private Point coordinates;
 */

    @Column(name = "timezone", nullable = false)
    private String timezone;

    @OneToMany(mappedBy = "arrivalAirport")
    private Set<Flight> arrivals;

    @OneToMany(mappedBy = "departureAirport")
    private Set<Flight> departures;

}
