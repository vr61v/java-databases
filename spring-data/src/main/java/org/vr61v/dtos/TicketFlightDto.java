package org.vr61v.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.vr61v.types.FareCondition;

@Data
public class TicketFlightDto {

    @Size(min = 13, max = 13)
    private String ticketNo;

    @Positive
    private Integer flightId;

    @NotNull
    private FareCondition fareConditions;

    @NotNull @Positive
    private Float amount;

}
