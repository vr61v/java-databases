package com.vr61v.entities;

import com.vr61v.entities.types.FlightStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "flights", schema = "bookings")
public class Flight {

    /**
     * To ensure proper ID generation in PostgreSQL, execute the following SQL to sync the sequence:
     * <pre>{@code
     * select setval('flights_flight_id_seq',
     *      (select max(flights.flight_id) from flights)
     * );
     * }</pre>
     * This sets the sequence's current value to the highest existing `flight_id`.
     */
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


    @Column(name = "scheduled_departure", nullable = false)
    private OffsetDateTime scheduledDeparture;

    @Column(name = "scheduled_arrival", nullable = false)
    private OffsetDateTime scheduledArrival;

    @Column(name = "actual_departure", nullable = false)
    private OffsetDateTime actualDeparture;

    @Column(name = "actual_arrival", nullable = false)
    private OffsetDateTime actualArrival;

}
