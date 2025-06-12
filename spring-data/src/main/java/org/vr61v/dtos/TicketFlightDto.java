package org.vr61v.dtos;

import lombok.Data;
import org.vr61v.types.FareCondition;

@Data
public class TicketFlightDto {
    private String ticketNo;
    private Integer flightId;
    private FareCondition fareConditions;
    private Float amount;
}
