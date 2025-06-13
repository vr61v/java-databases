package org.vr61v.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BoardingPassDto {

    @Size(min = 13, max = 13)
    private String ticketNo;

    @Positive
    private Integer flightId;

    @NotNull @Positive
    private Integer boardingNo;

    @NotNull @Size(min = 1, max = 4)
    private String seatNo;

}

