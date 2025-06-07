package org.vr61v.dtos;

import org.vr61v.embedded.LocalizedString;

public record AircraftDto (
        String aircraftCode,
        LocalizedString model,
        Integer range
) { }
