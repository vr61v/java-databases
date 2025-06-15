package org.vr61v.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vr61v.types.FlightStatus;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "flights", schema = "bookings")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flights_flight_id_seq")
    @SequenceGenerator(name = "flights_flight_id_seq", sequenceName = "flights_flight_id_seq", allocationSize = 1)
    @Column(name = "flight_id", nullable = false)
    private Integer flightId;

    @Column(name = "flight_no", length = 6, nullable = false)
    private String flightNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private FlightStatus status;

    @ManyToOne
    @JoinColumn(name = "aircraft_code", nullable = false)
    private Aircraft aircraft;

    @ManyToOne
    @JoinColumn(name = "departure_airport", nullable = false)
    private Airport departureAirport;

    @ManyToOne
    @JoinColumn(name = "arrival_airport", nullable = false)
    private Airport arrivalAirport;

    @Column(name = "scheduled_departure")
    private OffsetDateTime scheduledDeparture;

    @Column(name = "scheduled_arrival")
    private OffsetDateTime scheduledArrival;

    @Column(name = "actual_departure")
    private OffsetDateTime actualDeparture;

    @Column(name = "actual_arrival")
    private OffsetDateTime actualArrival;

}
