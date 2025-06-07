package org.vr61v.dtos;

public record BoardingPassDto (
        String ticketNo,
        String flightNo,
        Integer boardingNo,
        String seatNo
) { }
