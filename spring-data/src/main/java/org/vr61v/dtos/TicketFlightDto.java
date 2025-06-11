package org.vr61v.dtos;

import lombok.Data;
import org.vr61v.embedded.TicketFlightID;
import org.vr61v.types.FareCondition;

@Data
public class TicketFlightDto {
    private TicketFlightID id;
    private FareCondition fareConditions;
    private Float amount;
}
