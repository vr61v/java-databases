package org.vr61v.dtos;

import lombok.Data;
import org.vr61v.types.FlightStatus;

import java.time.OffsetDateTime;

@Data
public class FlightDto {
    private Integer flightId;
    private String flightNo;
    private FlightStatus status;

    private AircraftDto aircraft;
    private AirportDto departureAirport;
    private AirportDto arrivalAirport;

    private OffsetDateTime scheduledDeparture;
    private OffsetDateTime scheduledArrival;
    private OffsetDateTime actualDeparture;
    private OffsetDateTime actualArrival;
}

