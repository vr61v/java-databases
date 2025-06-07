package org.vr61v.dtos;

import org.vr61v.types.FareCondition;

public record SeatDto (
        String seatNo,
        FareCondition fareConditions
) { }
