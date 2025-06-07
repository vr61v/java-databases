package org.vr61v.dtos;

import org.vr61v.embedded.LocalizedString;

public record AirportDto(
        String airportCode,
        LocalizedString airportName,
        LocalizedString city,
        String timezone
) { }
