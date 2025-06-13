package org.vr61v.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.vr61v.types.FareCondition;

@Data
public class SeatDto {

    @Size(min = 3, max = 3)
    private String aircraftCode;

    @Size(min = 1, max = 4)
    private String seatNo;

    @NotNull
    private FareCondition fareConditions;

}
