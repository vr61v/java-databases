package org.vr61v.dtos;

import org.vr61v.embedded.TicketFlightID;
import org.vr61v.types.FareCondition;

public record TicketFlightDto (
        TicketFlightID id,
        FareCondition fareConditions,
        Float amount
) { }
