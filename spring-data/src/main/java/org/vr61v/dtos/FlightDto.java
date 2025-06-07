package org.vr61v.dtos;

import org.vr61v.types.FlightStatus;

import java.time.OffsetDateTime;

public record FlightDto (
        Integer flightId,
        String flightNo,
        FlightStatus status,

        AircraftDto aircraft,
        AirportDto departureAirport,
        AirportDto arrivalAirport,

        OffsetDateTime scheduledDeparture,
        OffsetDateTime scheduledArrival,
        OffsetDateTime actualDeparture,
        OffsetDateTime actualArrival
) { }
